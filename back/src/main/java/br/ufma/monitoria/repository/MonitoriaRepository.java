package br.ufma.monitoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.model.Monitoria;

@Repository
public interface MonitoriaRepository extends JpaRepository<Monitoria, Integer> {
}
