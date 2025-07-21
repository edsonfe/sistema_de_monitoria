package br.ufma.monitoria.service.impl;

import br.ufma.monitoria.model.Notificacao;
import br.ufma.monitoria.repository.NotificacaoRepository;
import br.ufma.monitoria.service.contract.NotificacaoServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService implements NotificacaoServiceInterface {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Override
    public Notificacao salvar(Notificacao notificacao) {
        return notificacaoRepository.save(notificacao);
    }

    @Override
    public Notificacao buscarPorId(Integer id) {
        return notificacaoRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioRuntime("Notificação não encontrada"));
    }

    @Override
    public List<Notificacao> listarTodas() {
        return notificacaoRepository.findAll();
    }
}
