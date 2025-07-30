package br.ufma.monitoria.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufma.monitoria.model.MensagemChat;
import br.ufma.monitoria.repository.MensagemChatRepository;
import br.ufma.monitoria.service.contract.MensagemChatServiceInterface;
import br.ufma.monitoria.service.exceptions.MensagemVaziaException;
import br.ufma.monitoria.service.exceptions.SessaoMonitoriaNotFoundException;
import br.ufma.monitoria.service.exceptions.UsuarioNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class MensagemChatService implements MensagemChatServiceInterface {

    @Autowired
    private MensagemChatRepository mensagemRepository;

    @Override
    @Transactional
    public MensagemChat salvarMensagem(MensagemChat mensagem) {
        if (mensagem.getConteudo() == null || mensagem.getConteudo().trim().isEmpty()) {
            throw new MensagemVaziaException("O conteúdo da mensagem não pode ser vazio.");
        }

        if (mensagem.getAutor() == null) {
            throw new UsuarioNotFoundException("Usuário autor da mensagem não foi informado.");
        }

        if (mensagem.getSessaoMonitoria() == null) {
            throw new SessaoMonitoriaNotFoundException("Sessão de monitoria da mensagem não foi informada.");
        }

        mensagem.setDataHora(LocalDateTime.now());
        return mensagemRepository.save(mensagem);
    }

    @Override
    public List<MensagemChat> listarMensagensPorSessao(UUID sessaoId) {
        List<MensagemChat> mensagens = mensagemRepository.findBySessaoMonitoriaIdOrderByDataHoraAsc(sessaoId);

        // Se quiser validar se não existe nenhuma mensagem
        if (mensagens.isEmpty()) {
            throw new MensagemVaziaException("Não há mensagens registradas para esta sessão.");
        }

        return mensagens;
    }
}
