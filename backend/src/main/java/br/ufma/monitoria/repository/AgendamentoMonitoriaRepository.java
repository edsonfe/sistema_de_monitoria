package br.ufma.monitoria.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufma.monitoria.model.AgendamentoMonitoria;

public interface AgendamentoMonitoriaRepository extends JpaRepository<AgendamentoMonitoria, UUID> {
    List<AgendamentoMonitoria> findByMonitoriaId(UUID monitoriaId);

    List<AgendamentoMonitoria> findByDia(LocalDate dia);

    Optional<AgendamentoMonitoria> findByMonitoriaIdAndDiaAndHorario(UUID monitoriaId, LocalDate dia,
            LocalTime horario);
}