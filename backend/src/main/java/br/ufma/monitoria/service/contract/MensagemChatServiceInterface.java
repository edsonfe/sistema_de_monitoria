package br.ufma.monitoria.service.contract;

import java.util.List;
import java.util.UUID;

import br.ufma.monitoria.model.MensagemChat;

public interface MensagemChatServiceInterface {
    MensagemChat salvarMensagem(MensagemChat mensagem);
    List<MensagemChat> listarMensagensPorSessao(UUID sessaoId);
}
