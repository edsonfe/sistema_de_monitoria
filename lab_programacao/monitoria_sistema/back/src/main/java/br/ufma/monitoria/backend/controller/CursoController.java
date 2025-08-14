package br.ufma.monitoria.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.backend.dto.CursoRequestDTO;
import br.ufma.monitoria.backend.dto.CursoResponseDTO;
import br.ufma.monitoria.backend.model.Curso;
import br.ufma.monitoria.backend.service.CursoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> listarTodos() {
        List<CursoResponseDTO> cursos = cursoService.listarTodos()
                .stream()
                .map(curso -> CursoResponseDTO.builder()
                        .cursoId(curso.getCursoId())
                        .nome(curso.getNome())
                        .codigo(curso.getCodigo())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            Curso curso = cursoService.buscarPorId(id);
            return ResponseEntity.ok(
                    CursoResponseDTO.builder()
                            .cursoId(curso.getCursoId())
                            .nome(curso.getNome())
                            .codigo(curso.getCodigo())
                            .build()
            );
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CursoResponseDTO> buscarPorCodigo(@PathVariable String codigo) {
        try {
            Curso curso = cursoService.buscarPorCodigo(codigo);
            return ResponseEntity.ok(
                    CursoResponseDTO.builder()
                            .cursoId(curso.getCursoId())
                            .nome(curso.getNome())
                            .codigo(curso.getCodigo())
                            .build()
            );
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody CursoRequestDTO dto) {
        try {
            Curso curso = Curso.builder()
                    .nome(dto.getNome())
                    .codigo(dto.getCodigo())
                    .build();

            Curso salvo = cursoService.cadastrarCurso(curso);

            return ResponseEntity.ok(
                    CursoResponseDTO.builder()
                            .cursoId(salvo.getCursoId())
                            .nome(salvo.getNome())
                            .codigo(salvo.getCodigo())
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        try {
            cursoService.removerCurso(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
