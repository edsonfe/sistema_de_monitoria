package br.ufma.monitoria.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.StatusSessao;
import br.ufma.monitoria.repository.SessaoMonitoriaRepository;
import br.ufma.monitoria.service.contract.SessaoMonitoriaServiceInterface;
import jakarta.transaction.Transactional;

@Service
public class SessaoMonitoriaService implements SessaoMonitoriaServiceInterface {

    @Autowired
    private SessaoMonitoriaRepository sessaoMonitoriaRepository;

    @Override
    @Transactional
    public SessaoMonitoria agendarSessao(SessaoMonitoria sessao) {
        // ✔️ Validação simples de conflito (pode refinar com horários)
        boolean conflito = sessaoMonitoriaRepository.existsByMonitoriaIdAndDataHora(
            sessao.getMonitoria().getId(), sessao.getDataHora());

        if (conflito) {
            throw new IllegalStateException("Já existe uma sessão marcada para este horário.");
        }

        sessao.setStatus(StatusSessao.AGENDADA);
        return sessaoMonitoriaRepository.save(sessao);
    }

    @Override
    public Optional<SessaoMonitoria> buscarPorId(UUID id) {
        return sessaoMonitoriaRepository.findById(id);
    }

    @Override
    public List<SessaoMonitoria> listarPorMonitoria(UUID monitoriaId) {
        return sessaoMonitoriaRepository.findByMonitoriaId(monitoriaId);
    }

    @Override
    public List<SessaoMonitoria> listarPorAluno(UUID alunoId) {
        return sessaoMonitoriaRepository.findByAlunoId(alunoId);
    }

    @Override
    @Transactional
    public SessaoMonitoria atualizarStatus(UUID sessaoId, StatusSessao novoStatus) {
        SessaoMonitoria sessao = sessaoMonitoriaRepository.findById(sessaoId)
            .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));

        sessao.setStatus(novoStatus);
        return sessaoMonitoriaRepository.save(sessao);
    }


}
