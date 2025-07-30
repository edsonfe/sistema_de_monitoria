package br.ufma.monitoria.service.contract;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import br.ufma.monitoria.model.AgendamentoMonitoria;

public interface AgendamentoMonitoriaServiceInterface {
    AgendamentoMonitoria criarAgendamento(UUID monitoriaId, LocalDate dia, LocalTime horario);

    List<AgendamentoMonitoria> buscarPorMonitoria(UUID monitoriaId);

    List<AgendamentoMonitoria> buscarPorDia(LocalDate dia);

    boolean existeAgendamento(UUID monitoriaId, LocalDate dia, LocalTime horario);

    AgendamentoMonitoria atualizarAgendamento(UUID id, UUID monitoriaId, LocalDate dia, LocalTime horario);

    void excluirAgendamento(UUID id);
}