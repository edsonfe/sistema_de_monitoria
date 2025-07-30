package br.ufma.monitoria.repository;

import br.ufma.monitoria.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {

    Optional<Avaliacao> findBySessaoMonitoriaId(UUID sessaoMonitoriaId);
}
