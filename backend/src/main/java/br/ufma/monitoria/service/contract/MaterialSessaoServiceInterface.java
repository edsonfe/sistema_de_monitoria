package br.ufma.monitoria.service.contract;

import java.util.List;
import java.util.UUID;

import br.ufma.monitoria.model.MaterialSessao;

public interface MaterialSessaoServiceInterface {
    MaterialSessao salvar(MaterialSessao material);
    List<MaterialSessao> buscarPorSessao(UUID sessaoId);
    void deletar(UUID materialId);
    MaterialSessao buscarPorId(UUID id);
}
