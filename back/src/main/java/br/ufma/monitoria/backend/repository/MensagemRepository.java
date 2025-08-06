package br.ufma.monitoria.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.backend.model.Mensagem;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    // Listar mensagens de uma sessão em ordem cronológica
    List<Mensagem> findBySessaoAgendada_SessaoIdOrderByDataHoraAsc(Long sessaoId);

    // Listar mensagens enviadas por um usuário específico em uma sessão
    List<Mensagem> findBySessaoAgendada_SessaoIdAndAutor_UsuarioIdOrderByDataHoraAsc(Long sessaoId, Long usuarioId);
}