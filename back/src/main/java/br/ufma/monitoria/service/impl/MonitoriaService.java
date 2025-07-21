package br.ufma.monitoria.service.impl;

import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.repository.MonitoriaRepository;
import br.ufma.monitoria.service.contract.MonitoriaServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoriaService implements MonitoriaServiceInterface {

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Override
    public Monitoria salvar(Monitoria monitoria) {
        return monitoriaRepository.save(monitoria);
    }

    @Override
    public Monitoria buscarPorId(Integer id) {
        return monitoriaRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioRuntime("Monitoria não encontrada"));
    }

    @Override
    public List<Monitoria> listarTodas() {
        return monitoriaRepository.findAll();
    }
}
