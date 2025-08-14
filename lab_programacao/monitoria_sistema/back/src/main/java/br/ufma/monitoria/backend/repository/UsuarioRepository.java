package br.ufma.monitoria.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.backend.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuário pelo e-mail (para login)
    Optional<Usuario> findByEmail(String email);

    // Verificar se matrícula já existe (para evitar duplicidade no cadastro)
    boolean existsByMatricula(String matricula);

    // Verificar se e-mail já existe (para cadastro único)
    boolean existsByEmail(String email);
}