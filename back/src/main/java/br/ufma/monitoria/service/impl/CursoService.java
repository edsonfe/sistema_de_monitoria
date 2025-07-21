package br.ufma.monitoria.service.impl;

import br.ufma.monitoria.model.Curso;
import br.ufma.monitoria.repository.CursoRepository;
import br.ufma.monitoria.service.contract.CursoServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService implements CursoServiceInterface {

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public Curso salvar(Curso curso) {
        return cursoRepository.save(curso);
    }

    public Curso listarPorId(Integer id) {
        return cursoRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioRuntime("Curso não encontrado"));
    }

    @Override
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    @Override
    public Curso buscarPorId(Integer id) {
        return cursoRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioRuntime("Curso não encontrado"));
    }

}
