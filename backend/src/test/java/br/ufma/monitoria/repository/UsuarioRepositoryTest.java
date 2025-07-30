package br.ufma.monitoria.repository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveEncontrarUsuarioPorMatricula() {
        Usuario usuario = Usuario.builder()
            .nome("Bruno")
            .email("bruno@ufma.br")
            .senha("12345")
            .tipo(TipoUsuario.ALUNO)
            .matricula("2023101001")
            .dataNascimento(LocalDate.of(2000, 1, 1))
            .build();

        usuarioRepository.save(usuario);

        Optional<Usuario> resultado = usuarioRepository.findByMatricula("2023101001");
        assertTrue(resultado.isPresent());
        assertEquals("Bruno", resultado.get().getNome());
    }
}
