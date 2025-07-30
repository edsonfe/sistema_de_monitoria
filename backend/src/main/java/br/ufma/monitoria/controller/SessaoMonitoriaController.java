package br.ufma.monitoria.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.dto.SessaoMonitoriaDTO;
import br.ufma.monitoria.dto.SessaoMonitoriaMapper;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.MonitoriaRepository;
import br.ufma.monitoria.repository.SessaoMonitoriaRepository;
import br.ufma.monitoria.repository.UsuarioRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sessoes")
public class SessaoMonitoriaController {

    @Autowired
    private SessaoMonitoriaRepository sessaoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private MonitoriaRepository monitoriaRepo;

    @Autowired
    private SessaoMonitoriaMapper mapper;

    @GetMapping
    public ResponseEntity<List<SessaoMonitoriaDTO>> listar() {
        List<SessaoMonitoriaDTO> dtos = sessaoRepo.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();

        return dtos.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<SessaoMonitoriaDTO> criar(@Valid @RequestBody SessaoMonitoriaDTO dto) {
        Optional<Usuario> alunoOpt = usuarioRepo.findById(dto.getAlunoId());
        Optional<Monitoria> monitoriaOpt = monitoriaRepo.findById(dto.getMonitoriaId());

        if (alunoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null); // Ou usar body("Aluno não encontrado") se quiser texto simples
        }

        if (monitoriaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
        }

        SessaoMonitoria sessao = mapper.toEntity(dto, monitoriaOpt.get(), alunoOpt.get());
        SessaoMonitoriaDTO resposta = mapper.toDTO(sessaoRepo.save(sessao));
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoMonitoriaDTO> buscar(@PathVariable UUID id) {
        return sessaoRepo.findById(id)
            .map(mapper::toDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        return sessaoRepo.findById(id)
            .map(sessao -> {
                sessaoRepo.delete(sessao);
                return ResponseEntity.ok().<Void>build();
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/por-monitoria/{monitoriaId}")
    public ResponseEntity<List<SessaoMonitoriaDTO>> listarPorMonitoria(@PathVariable UUID monitoriaId) {
        List<SessaoMonitoriaDTO> dtos = sessaoRepo.findByMonitoriaId(monitoriaId)
            .stream()
            .map(mapper::toDTO)
            .toList();

        return dtos.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(dtos);
    }

    @GetMapping("/por-aluno/{alunoId}")
    public ResponseEntity<List<SessaoMonitoriaDTO>> listarPorAluno(@PathVariable UUID alunoId) {
        List<SessaoMonitoriaDTO> dtos = sessaoRepo.findByAlunoId(alunoId)
            .stream()
            .map(mapper::toDTO)
            .toList();

        return dtos.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(dtos);
    }
}
