package br.ufma.monitoria.backend.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.backend.model.Avaliacao;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // Listar todas as avaliações de uma sessão
    List<Avaliacao> findBySessaoAgendada_SessaoId(Long sessaoId);

    // Listar todas as avaliações feitas por um aluno específico
    List<Avaliacao> findByAluno_UsuarioId(Long alunoId);

    // Calcular a média de estrelas de uma sessão (para uso em service)
    // Exemplo de query derivada para pegar só as estrelas
    List<Avaliacao> findBySessaoAgendada_SessaoIdAndEstrelasNotNull(Long sessaoId);
}