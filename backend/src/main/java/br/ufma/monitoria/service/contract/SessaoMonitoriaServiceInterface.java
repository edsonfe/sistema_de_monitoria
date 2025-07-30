package br.ufma.monitoria.service.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.StatusSessao;

public interface SessaoMonitoriaServiceInterface {
    SessaoMonitoria agendarSessao(SessaoMonitoria sessao);
    Optional<SessaoMonitoria> buscarPorId(UUID id);
    List<SessaoMonitoria> listarPorMonitoria(UUID monitoriaId);
    List<SessaoMonitoria> listarPorAluno(UUID alunoId);
    SessaoMonitoria atualizarStatus(UUID sessaoId, StatusSessao novoStatus);
}
