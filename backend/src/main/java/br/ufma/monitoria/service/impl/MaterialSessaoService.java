package br.ufma.monitoria.service.impl;

import br.ufma.monitoria.model.MaterialSessao;
import br.ufma.monitoria.repository.MaterialSessaoRepository;
import br.ufma.monitoria.service.contract.MaterialSessaoServiceInterface;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialSessaoService implements MaterialSessaoServiceInterface {
    
    private final MaterialSessaoRepository repository;

    @Override
    @Transactional
    public MaterialSessao salvar(MaterialSessao material) {
        if (material.getTitulo() == null || material.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título do material não pode estar vazio");
        }
        if (material.getDataEnvio() == null) {
            throw new IllegalArgumentException("A data de envio deve ser informada");
        }
        return repository.save(material);
    }

    @Override
    public List<MaterialSessao> buscarPorSessao(UUID sessaoId) {
        return repository.findBySessaoMonitoriaId(sessaoId);
    }

    @Override
    public void deletar(UUID materialId) {
        repository.deleteById(materialId);
    }

    @Override
    public MaterialSessao buscarPorId(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Material não encontrado"));
    }
}
