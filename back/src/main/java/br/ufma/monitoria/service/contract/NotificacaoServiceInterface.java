package br.ufma.monitoria.service.contract;

import br.ufma.monitoria.model.Notificacao;
import java.util.List;

public interface NotificacaoServiceInterface {
    Notificacao salvar(Notificacao notificacao);
    Notificacao buscarPorId(Integer id);
    List<Notificacao> listarTodas();
}
