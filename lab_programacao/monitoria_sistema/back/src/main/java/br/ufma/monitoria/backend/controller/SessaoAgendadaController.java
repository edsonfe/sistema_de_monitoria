package br.ufma.monitoria.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.backend.dto.SessaoAgendadaRequestDTO;
import br.ufma.monitoria.backend.dto.SessaoAgendadaResponseDTO;
import br.ufma.monitoria.backend.model.Monitoria;
import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.model.StatusSessao;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.service.MonitoriaService;
import br.ufma.monitoria.backend.service.SessaoAgendadaService;
import br.ufma.monitoria.backend.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sessoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SessaoAgendadaController {

    private final SessaoAgendadaService sessaoService;
    private final UsuarioService usuarioService;
    private final MonitoriaService monitoriaService;

    // ------------------- LISTAGENS -------------------

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<SessaoAgendadaResponseDTO>> listarPorAluno(@PathVariable Long alunoId) {
        List<SessaoAgendadaResponseDTO> lista = sessaoService.listarPorAluno(alunoId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/monitor/{monitorId}")
    public ResponseEntity<List<SessaoAgendadaResponseDTO>> listarPorMonitor(@PathVariable Long monitorId) {
        List<SessaoAgendadaResponseDTO> lista = sessaoService.listarPorMonitor(monitorId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SessaoAgendadaResponseDTO>> listarPorStatus(@PathVariable StatusSessao status) {
        List<SessaoAgendadaResponseDTO> lista = sessaoService.listarPorStatus(status)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/futuras/aluno/{alunoId}")
    public ResponseEntity<List<SessaoAgendadaResponseDTO>> listarFuturasPorAluno(@PathVariable Long alunoId) {
        List<SessaoAgendadaResponseDTO> lista = sessaoService.listarFuturasPorAluno(alunoId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/futuras/monitor/{monitorId}")
    public ResponseEntity<List<SessaoAgendadaResponseDTO>> listarFuturasPorMonitor(@PathVariable Long monitorId) {
        List<SessaoAgendadaResponseDTO> lista = sessaoService.listarFuturasPorMonitor(monitorId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoAgendadaResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            SessaoAgendada sessao = sessaoService.buscarPorId(id);
            return ResponseEntity.ok(toResponseDTO(sessao));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------- CRIAÇÃO -------------------

    @PostMapping
    public ResponseEntity<?> agendar(@RequestBody SessaoAgendadaRequestDTO dto) {
        try {
            Usuario aluno = usuarioService.buscarPorId(dto.getAlunoId());
            Monitoria monitoria = monitoriaService.buscarPorId(dto.getMonitoriaId());

            SessaoAgendada novaSessao = SessaoAgendada.builder()
                    .aluno(aluno)
                    .monitoria(monitoria)
                    .data(dto.getData())
                    .status(dto.getStatus())
                    .build();

            SessaoAgendada salva = sessaoService.agendarSessao(novaSessao);
            return ResponseEntity.ok(toResponseDTO(salva));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Aluno ou monitoria não encontrado.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------------------- ATUALIZAÇÃO -------------------

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam StatusSessao status) {
        try {
            SessaoAgendada atualizada = sessaoService.atualizarStatus(id, status);
            return ResponseEntity.ok(toResponseDTO(atualizada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // ------------------- CANCELAMENTO -------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            sessaoService.cancelarSessao(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------- MÉTODO DE CONVERSÃO -------------------

    private SessaoAgendadaResponseDTO toResponseDTO(SessaoAgendada sessao) {
        return SessaoAgendadaResponseDTO.builder()
                .sessaoId(sessao.getSessaoId())
                .data(sessao.getData())
                .status(sessao.getStatus())
                .alunoId(sessao.getAluno().getUsuarioId())
                .alunoNome(sessao.getAluno().getNome())
                .monitoriaId(sessao.getMonitoria().getMonitoriaId())
                .disciplinaMonitoria(sessao.getMonitoria().getDisciplina())
                .monitorNome(sessao.getMonitoria().getMonitor().getNome()) // ADICIONADO
                .linkSalaVirtual(sessao.getMonitoria().getLinkSalaVirtual()) // ADICIONADO
                .build();
    }

}
