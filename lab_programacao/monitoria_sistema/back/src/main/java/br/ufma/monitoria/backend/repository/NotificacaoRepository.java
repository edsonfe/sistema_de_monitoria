package br.ufma.monitoria.backend.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.backend.model.Notificacao;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    // Listar todas as notificações de um usuário específico
    List<Notificacao> findByUsuarioDestino_UsuarioId(Long usuarioId);

    // Listar notificações não lidas de um usuário
    List<Notificacao> findByUsuarioDestino_UsuarioIdAndLidaFalse(Long usuarioId);
}