package br.ufma.monitoria.service.contract;

import br.ufma.monitoria.model.Monitoria;
import java.util.List;

public interface MonitoriaServiceInterface {
    Monitoria salvar(Monitoria monitoria);
    Monitoria buscarPorId(Integer id);
    List<Monitoria> listarTodas();
}
