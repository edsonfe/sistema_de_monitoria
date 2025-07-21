package br.ufma.monitoria.service.impl;

import br.ufma.monitoria.model.Atividade;
import br.ufma.monitoria.repository.AtividadeRepository;
import br.ufma.monitoria.service.contract.AtividadeServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtividadeService implements AtividadeServiceInterface {

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Override
    public Atividade salvar(Atividade atividade) {
        return atividadeRepository.save(atividade);
    }

    @Override
    public Atividade buscarPorId(Integer id) {
        return atividadeRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioRuntime("Atividade não encontrada"));
    }

    @Override
    public List<Atividade> listarTodas() {
        return atividadeRepository.findAll();
    }
}
