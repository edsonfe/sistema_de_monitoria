package br.ufma.monitoria.backend.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.backend.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Buscar curso pelo código (para seleção rápida no cadastro de monitoria)
    Optional<Curso> findByCodigo(String codigo);

    // Buscar curso pelo nome
    Optional<Curso> findByNome(String nome);

    // Verificar se já existe um curso com o mesmo código
    boolean existsByCodigo(String codigo);
}