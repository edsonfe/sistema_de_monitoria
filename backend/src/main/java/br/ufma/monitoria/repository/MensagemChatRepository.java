package br.ufma.monitoria.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufma.monitoria.model.MensagemChat;

public interface MensagemChatRepository extends JpaRepository<MensagemChat, UUID> {

    List<MensagemChat> findBySessaoMonitoriaId(UUID sessaoMonitoriaId);
    List<MensagemChat> findBySessaoMonitoriaIdOrderByDataHoraAsc(UUID sessaoMonitoriaId);

}
