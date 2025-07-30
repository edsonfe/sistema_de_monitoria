package br.ufma.monitoria.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufma.monitoria.dto.MonitoriaCadastroDTO;
import br.ufma.monitoria.dto.MonitoriaMapper;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.MonitoriaRepository;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.service.contract.MonitoriaServiceInterface;

@Service
public class MonitoriaService implements MonitoriaServiceInterface {

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MonitoriaMapper mapper;

    @Override
    public Monitoria criar(MonitoriaCadastroDTO dto) {
        Usuario monitor = usuarioRepository.findById(dto.getMonitorId())
            .orElseThrow(() -> new RuntimeException("Monitor não encontrado"));

        Monitoria monitoria = mapper.toEntity(dto, monitor);
        return monitoriaRepository.save(monitoria);
    }

    @Override
    public Optional<Monitoria> buscarPorId(UUID id) {
        return monitoriaRepository.findById(id);
    }

    @Override
    public List<Monitoria> listarTodos() {
        return monitoriaRepository.findAll();
    }

    @Override
    public List<Monitoria> listarPorCurso(String curso) {
        return monitoriaRepository.findByCurso(curso);
    }

    @Override
    public List<Monitoria> listarPorDisciplina(String disciplina) {
        return monitoriaRepository.findByDisciplina(disciplina);
    }

    @Override
    public List<Monitoria> listarPorMonitor(UUID monitorId) {
        return monitoriaRepository.findByMonitorId(monitorId);
    }

    @Override
    public void deletar(UUID id) {
        monitoriaRepository.deleteById(id);
    }
}
