package br.ufma.monitoria.service.contract;

import br.ufma.monitoria.model.Atividade;
import java.util.List;

public interface AtividadeServiceInterface {
    Atividade salvar(Atividade atividade);
    Atividade buscarPorId(Integer id);
    List<Atividade> listarTodas();
}
