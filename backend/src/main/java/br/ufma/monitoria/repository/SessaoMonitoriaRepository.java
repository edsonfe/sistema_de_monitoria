package br.ufma.monitoria.repository;

import br.ufma.monitoria.model.SessaoMonitoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SessaoMonitoriaRepository extends JpaRepository<SessaoMonitoria, UUID> {

    List<SessaoMonitoria> findByMonitoriaId(UUID monitoriaId);

    List<SessaoMonitoria> findByAlunoId(UUID alunoId);

    boolean existsByMonitoriaIdAndDataHora(UUID monitoriaId, LocalDateTime dataHora);
}
