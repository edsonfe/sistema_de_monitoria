package br.ufma.monitoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.model.Atividade;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Integer> {
}
