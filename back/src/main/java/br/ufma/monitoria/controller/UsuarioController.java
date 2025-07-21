package br.ufma.monitoria.controller;

import br.ufma.monitoria.dto.UsuarioDTO;
import br.ufma.monitoria.model.*;
import br.ufma.monitoria.repository.*;
import br.ufma.monitoria.service.contract.UsuarioServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceInterface usuarioService;
    private final CursoRepository cursoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final MonitoriaRepository monitoriaRepository;

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody UsuarioDTO dto) {
        Curso curso = cursoRepository.findById(dto.getCursoId())
            .orElseThrow(() -> new RegraNegocioRuntime("Curso não encontrado"));

        Usuario usuario = Usuario.builder()
            .nome(dto.getNome())
            .email(dto.getEmail())
            .senha(dto.getSenha())
            .matricula(dto.getMatricula())
            .dataNascimento(dto.getDataNascimento())
            .tipo(dto.getTipo())
            .codigoVerificacao(dto.getCodigoVerificacao())
            .curso(curso)
            .build();

        if (dto.getTipo() == TipoUsuario.MONITOR && dto.getDisciplinaMonitoradaId() != null) {
            Disciplina disciplina = disciplinaRepository.findById(dto.getDisciplinaMonitoradaId())
                .orElseThrow(() -> new RegraNegocioRuntime("Disciplina não encontrada"));
            usuario.setDisciplina(disciplina);
        }

        if (dto.getTipo() == TipoUsuario.ALUNO) {
            if (dto.getDisciplinasIds() != null) {
                Set<Disciplina> disciplinas = dto.getDisciplinasIds().stream()
                    .map(id -> disciplinaRepository.findById(id)
                        .orElseThrow(() -> new RegraNegocioRuntime("Disciplina não encontrada: " + id)))
                    .collect(Collectors.toSet());
                usuario.setDisciplinas(disciplinas);
            }

            if (dto.getMonitoriaIds() != null) {
                Set<Monitoria> monitorias = dto.getMonitoriaIds().stream()
                    .map(id -> monitoriaRepository.findById(id)
                        .orElseThrow(() -> new RegraNegocioRuntime("Monitoria não encontrada: " + id)))
                    .collect(Collectors.toSet());
                usuario.setMonitorias(monitorias);
            }
        }

        Usuario salvo = usuarioService.salvar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}
