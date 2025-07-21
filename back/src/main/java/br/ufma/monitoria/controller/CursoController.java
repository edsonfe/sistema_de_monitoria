package br.ufma.monitoria.controller;

import br.ufma.monitoria.dto.CursoDTO;
import br.ufma.monitoria.model.Curso;
import br.ufma.monitoria.service.contract.CursoServiceInterface;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoServiceInterface cursoService;

    @PostMapping
    public ResponseEntity<Curso> criar(@Valid @RequestBody CursoDTO dto) {
        Curso curso = Curso.builder().nome(dto.getNome()).build();
        Curso salvo = cursoService.salvar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Integer id) {
        Curso curso = cursoService.buscarPorId(id);
        return ResponseEntity.ok(curso);
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listarTodos() {
        return ResponseEntity.ok(cursoService.listarTodos());
    }
}
