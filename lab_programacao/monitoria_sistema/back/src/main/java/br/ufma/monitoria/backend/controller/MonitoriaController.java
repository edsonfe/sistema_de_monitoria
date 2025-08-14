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
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.backend.dto.MonitoriaRequestDTO;
import br.ufma.monitoria.backend.dto.MonitoriaResponseDTO;
import br.ufma.monitoria.backend.model.Curso;
import br.ufma.monitoria.backend.model.Monitoria;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.service.CursoService;
import br.ufma.monitoria.backend.service.MonitoriaService;
import br.ufma.monitoria.backend.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/monitorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MonitoriaController {

    private final MonitoriaService monitoriaService;
    private final UsuarioService usuarioService;
    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<MonitoriaResponseDTO>> listarTodas() {
        List<Monitoria> monitorias = monitoriaService.buscarPorParteDisciplina("");
        List<MonitoriaResponseDTO> dtos = monitorias.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            Monitoria monitoria = monitoriaService.buscarPorId(id);
            return ResponseEntity.ok(toResponseDTO(monitoria));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody MonitoriaRequestDTO dto) {
        try {
            Usuario monitor = usuarioService.buscarPorId(dto.getMonitorId());
            Curso curso = cursoService.buscarPorId(dto.getCursoId());

            Monitoria monitoria = toEntity(dto, monitor, curso);
            Monitoria salva = monitoriaService.criarMonitoria(monitoria);

            return ResponseEntity.ok(toResponseDTO(salva));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Monitor ou curso não encontrado.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody MonitoriaRequestDTO dto) {
        try {
            Usuario monitor = usuarioService.buscarPorId(dto.getMonitorId());
            Curso curso = cursoService.buscarPorId(dto.getCursoId());

            Monitoria monitoria = toEntity(dto, monitor, curso);
            monitoria.setMonitoriaId(id);

            Monitoria atualizada = monitoriaService.atualizarMonitoria(monitoria);
            return ResponseEntity.ok(toResponseDTO(atualizada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Monitoria, monitor ou curso não encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        try {
            monitoriaService.removerMonitoria(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Conversão de entidade para DTO de resposta
    private MonitoriaResponseDTO toResponseDTO(Monitoria m) {
        return MonitoriaResponseDTO.builder()
                .monitoriaId(m.getMonitoriaId())
                .disciplina(m.getDisciplina())
                .codigoDisciplina(m.getCodigoDisciplina())
                .diasDaSemana(m.getDiasDaSemana())
                .horario(m.getHorario())
                .linkSalaVirtual(m.getLinkSalaVirtual())
                .observacoes(m.getObservacoes())
                .monitorId(m.getMonitor().getUsuarioId())
                .monitorNome(m.getMonitor().getNome())
                .cursoId(m.getCurso().getCursoId())
                .cursoNome(m.getCurso().getNome())
                .build();
    }

    // Conversão do DTO de requisição para entidade
    private Monitoria toEntity(MonitoriaRequestDTO dto, Usuario monitor, Curso curso) {
        return Monitoria.builder()
                .disciplina(dto.getDisciplina())
                .codigoDisciplina(dto.getCodigoDisciplina())
                .diasDaSemana(dto.getDiasDaSemana())
                .horario(dto.getHorario())
                .linkSalaVirtual(dto.getLinkSalaVirtual())
                .observacoes(dto.getObservacoes())
                .monitor(monitor)
                .curso(curso)
                .build();
    }
}
