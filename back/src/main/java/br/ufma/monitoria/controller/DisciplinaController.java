package br.ufma.monitoria.controller;

import br.ufma.monitoria.dto.DisciplinaDTO;
import br.ufma.monitoria.model.Disciplina;
import br.ufma.monitoria.service.contract.DisciplinaServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplinas")
@RequiredArgsConstructor
public class DisciplinaController {

    private final DisciplinaServiceInterface disciplinaService;

    @PostMapping
    public ResponseEntity<Disciplina> criar(@Valid @RequestBody DisciplinaDTO dto) {
        Disciplina disciplina = Disciplina.builder()
            .nome(dto.getNome())
            .build();

        Disciplina salva = disciplinaService.salvar(disciplina);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disciplina> buscarPorId(@PathVariable Integer id) {
        Disciplina d = disciplinaService.buscarPorId(id);
        return ResponseEntity.ok(d);
    }

    @GetMapping
    public ResponseEntity<List<Disciplina>> listarTodos() {
        return ResponseEntity.ok(disciplinaService.listarTodas());
    }
}
