package br.ufma.monitoria.service.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufma.monitoria.model.MaterialMonitoria;
import br.ufma.monitoria.repository.MaterialMonitoriaRepository;
import br.ufma.monitoria.service.contract.MaterialMonitoriaServiceInterface;

@Service
public class MaterialMonitoriaService implements MaterialMonitoriaServiceInterface {

    @Autowired
    public MaterialMonitoriaRepository repository;

    @Override
    public MaterialMonitoria salvar(MaterialMonitoria material) {
        return repository.save(material);
    }

    @Override
    public List<MaterialMonitoria> listarPorMonitoria(UUID monitoriaId) {
        return repository.findByMonitoriaId(monitoriaId);
    }

    @Override
    public void excluir(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public MaterialMonitoria buscarPorId(UUID id) {
        return repository.findById(id).orElse(null);
    }
}
