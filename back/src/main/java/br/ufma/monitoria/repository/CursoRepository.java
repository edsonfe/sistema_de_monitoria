package br.ufma.monitoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
}
