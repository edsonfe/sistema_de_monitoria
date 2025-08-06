package br.ufma.monitoria.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufma.monitoria.backend.model.Curso;
import br.ufma.monitoria.backend.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    @Transactional
    public Curso cadastrarCurso(Curso curso) {
        if (cursoRepository.existsByCodigo(curso.getCodigo())) {
            throw new IllegalArgumentException("Código de curso já cadastrado.");
        }
        return cursoRepository.save(curso);
    }

    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));
    }

    public Curso buscarPorCodigo(String codigo) {
        return cursoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado pelo código"));
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    @Transactional
    public void removerCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Curso não encontrado para exclusão");
        }
        cursoRepository.deleteById(id);
    }
}
