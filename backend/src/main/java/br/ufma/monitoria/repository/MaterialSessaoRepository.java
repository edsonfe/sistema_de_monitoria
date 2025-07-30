package br.ufma.monitoria.repository;

import br.ufma.monitoria.model.MaterialSessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MaterialSessaoRepository extends JpaRepository<MaterialSessao, UUID> {

    List<MaterialSessao> findBySessaoMonitoriaId(UUID sessaoMonitoriaId);
}
