package br.ufma.monitoria.service.impl;

import br.ufma.monitoria.model.Disciplina;
import br.ufma.monitoria.repository.DisciplinaRepository;
import br.ufma.monitoria.service.contract.DisciplinaServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinaService implements DisciplinaServiceInterface {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        return disciplinaRepository.save(disciplina);
    }

    @Override
    public Disciplina buscarPorId(Integer id) {
        return disciplinaRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioRuntime("Disciplina não encontrada"));
    }

    @Override
    public List<Disciplina> listarTodas() {
        return disciplinaRepository.findAll();
    }
}
