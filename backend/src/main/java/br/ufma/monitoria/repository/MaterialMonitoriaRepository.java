package br.ufma.monitoria.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufma.monitoria.model.MaterialMonitoria;

public interface MaterialMonitoriaRepository extends JpaRepository<MaterialMonitoria, UUID> {
    List<MaterialMonitoria> findByMonitoriaId(UUID monitoriaId);
}
