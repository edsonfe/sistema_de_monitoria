package br.ufma.monitoria.service.contract;

import br.ufma.monitoria.model.Sessao;
import java.util.List;

public interface SessaoServiceInterface {
    Sessao salvar(Sessao sessao);
    Sessao buscarPorId(Integer id);
    List<Sessao> listarTodas();
}
