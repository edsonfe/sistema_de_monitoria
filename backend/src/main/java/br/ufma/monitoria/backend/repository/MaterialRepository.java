package br.ufma.monitoria.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.backend.model.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    // Listar todos os materiais de uma sessão específica
    List<Material> findBySessaoAgendada_SessaoId(Long sessaoId);

    // Buscar materiais enviados após uma data específica (útil para notificações)
    List<Material> findBySessaoAgendada_SessaoIdAndDataEnvioAfter(Long sessaoId, java.time.LocalDateTime dataEnvio);
}