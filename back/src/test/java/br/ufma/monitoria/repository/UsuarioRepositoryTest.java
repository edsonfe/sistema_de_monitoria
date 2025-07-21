package br.ufma.monitoria.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;

@SpringBootTest
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

  @Test
  void deveSalvarEValidarUsuario() {
      Usuario usuario = Usuario.builder()
          .nome("Maria")
          .email("maria@email.com")
          .senha("123456")
          .tipo(TipoUsuario.ALUNO)
          .build();

      Usuario salvo = usuarioRepository.save(usuario);

      assertNotNull(salvo);
      assertEquals("Maria", salvo.getNome());
      assertEquals("maria@email.com", salvo.getEmail());
      assertEquals("123456", salvo.getSenha());
      assertEquals(TipoUsuario.ALUNO, salvo.getTipo());
  }

}
