package br.ufma.monitoria.service.impl;

import br.ufma.monitoria.model.Sessao;
import br.ufma.monitoria.repository.SessaoRepository;
import br.ufma.monitoria.service.contract.SessaoServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessaoService implements SessaoServiceInterface {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Override
    public Sessao salvar(Sessao sessao) {
        return sessaoRepository.save(sessao);
    }

    @Override
    public Sessao buscarPorId(Integer id) {
        return sessaoRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioRuntime("Sessão não encontrada"));
    }

    @Override
    public List<Sessao> listarTodas() {
        return sessaoRepository.findAll();
    }
}
