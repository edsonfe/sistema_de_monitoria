package br.ufma.monitoria.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.model.StatusSessao;
import br.ufma.monitoria.backend.repository.SessaoAgendadaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessaoAgendadaService {

    private final SessaoAgendadaRepository sessaoRepository;

    @Transactional
    public SessaoAgendada agendarSessao(SessaoAgendada sessao) {
        if (sessao.getData() == null || sessao.getData().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data da sessão deve ser futura");
        }
        return sessaoRepository.save(sessao);
    }

    public SessaoAgendada buscarPorId(Long id) {
        return sessaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));
    }

    public List<SessaoAgendada> listarPorAluno(Long alunoId) {
        return sessaoRepository.findByAluno_UsuarioId(alunoId);
    }

    public List<SessaoAgendada> listarPorMonitor(Long monitorId) {
        return sessaoRepository.findByMonitoria_Monitor_UsuarioId(monitorId);
    }

    public List<SessaoAgendada> listarPorStatus(StatusSessao status) {
        return sessaoRepository.findByStatus(status);
    }

    public List<SessaoAgendada> listarFuturasPorAluno(Long alunoId) {
        return sessaoRepository.findByAluno_UsuarioIdAndDataAfter(alunoId, LocalDateTime.now());
    }

    public List<SessaoAgendada> listarFuturasPorMonitor(Long monitorId) {
        return sessaoRepository.findByMonitoria_Monitor_UsuarioIdAndDataAfter(monitorId, LocalDateTime.now());
    }

    @Transactional
    public SessaoAgendada atualizarStatus(Long id, StatusSessao novoStatus) {
        SessaoAgendada sessao = buscarPorId(id);
        sessao.setStatus(novoStatus);
        return sessaoRepository.save(sessao);
    }

    @Transactional
    public void cancelarSessao(Long id) {
        if (!sessaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Sessão não encontrada para cancelamento");
        }
        sessaoRepository.deleteById(id);
    }
}
