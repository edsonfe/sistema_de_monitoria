package br.ufma.monitoria.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufma.monitoria.backend.model.Monitoria;
import br.ufma.monitoria.backend.repository.MonitoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonitoriaService {

    private final MonitoriaRepository monitoriaRepository;

    @Transactional
    public Monitoria criarMonitoria(Monitoria monitoria) {

        return monitoriaRepository.save(monitoria);
    }

    public Monitoria buscarPorId(Long id) {
        return monitoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monitoria não encontrada"));
    }

    public List<Monitoria> buscarPorCurso(Long cursoId) {
        return monitoriaRepository.findByCurso_CursoId(cursoId);
    }

    public List<Monitoria> buscarPorMonitor(Long monitorId) {
        return monitoriaRepository.findByMonitor_UsuarioId(monitorId);
    }

    public List<Monitoria> buscarPorCodigoDisciplina(String codigoDisciplina) {
        return monitoriaRepository.findByCodigoDisciplina(codigoDisciplina);
    }

    public List<Monitoria> buscarPorParteDisciplina(String disciplina) {
        return monitoriaRepository.findByDisciplinaContainingIgnoreCase(disciplina);
    }

    @Transactional
    public Monitoria atualizarMonitoria(Monitoria monitoria) {
        if (!monitoriaRepository.existsById(monitoria.getMonitoriaId())) {
            throw new EntityNotFoundException("Monitoria não encontrada para atualização");
        }
        return monitoriaRepository.save(monitoria);
    }

    @Transactional
    public void removerMonitoria(Long id) {
        if (!monitoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Monitoria não encontrada para exclusão");
        }
        monitoriaRepository.deleteById(id);
    }
}
