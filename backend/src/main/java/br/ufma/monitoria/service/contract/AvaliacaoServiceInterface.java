package br.ufma.monitoria.service.contract;

import br.ufma.monitoria.model.Avaliacao;

import java.util.Optional;
import java.util.UUID;

public interface AvaliacaoServiceInterface {
    Avaliacao salvarAvaliacao(Avaliacao avaliacao);
    Optional<Avaliacao> buscarPorSessaoMonitoriaId(UUID sessaoMonitoriaId);
    Double calcularMediaPorMonitoria(UUID monitoriaId);
}
