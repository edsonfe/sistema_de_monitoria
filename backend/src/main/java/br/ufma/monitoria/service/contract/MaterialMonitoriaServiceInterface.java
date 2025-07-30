package br.ufma.monitoria.service.contract;

import java.util.List;
import java.util.UUID;
import br.ufma.monitoria.model.MaterialMonitoria;

public interface MaterialMonitoriaServiceInterface {
    MaterialMonitoria salvar(MaterialMonitoria material);
    List<MaterialMonitoria> listarPorMonitoria(UUID monitoriaId);
    void excluir(UUID id);
    MaterialMonitoria buscarPorId(UUID id);
}