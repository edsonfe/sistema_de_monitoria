package br.ufma.monitoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.model.Sessao;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Integer> {
}
