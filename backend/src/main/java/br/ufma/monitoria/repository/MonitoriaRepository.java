package br.ufma.monitoria.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufma.monitoria.model.Monitoria;

public interface MonitoriaRepository extends JpaRepository<Monitoria, UUID> {
    List<Monitoria> findByCurso(String curso);

    List<Monitoria> findByDisciplina(String disciplina);

    List<Monitoria> findByMonitorId(UUID monitorId);
}