package br.ufma.monitoria.backend.repository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.ufma.monitoria.backend.model.Curso;
import br.ufma.monitoria.backend.model.TipoUsuario;
import br.ufma.monitoria.backend.model.Usuario;

@DataJpaTest
class UsuarioRepositoryTest {

  @Autowired
  private CursoRepository cursoRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Test
  void deveSalvarEBuscarUsuarioPorEmail() {
    // Criar e salvar curso
    Curso curso = Curso.builder()
        .nome("Ciência da Computação")
        .codigo("CCOMP")
        .build();
    cursoRepository.save(curso);

    // Criar usuário com curso associado
    Usuario usuario = Usuario.builder()
        .nome("João da Silva")
        .email("joao@email.com")
        .celular("11999999999")
        .dataNascimento(LocalDate.of(2000, 1, 1))
        .curso(curso)
        .matricula("20250001")
        .tipoUsuario(TipoUsuario.ALUNO)
        .senha("123456")
        .build();

    usuarioRepository.save(usuario);

    Usuario encontrado = usuarioRepository.findByEmail("joao@email.com").orElse(null);

    assertThat(encontrado).isNotNull();
    assertThat(encontrado.getMatricula()).isEqualTo("20250001");
  }

  @Test
  void deveDetectarMatriculaDuplicada() {
    // Criar e salvar curso
    Curso curso = Curso.builder()
        .nome("Engenharia de Software")
        .codigo("ENGSW")
        .build();
    cursoRepository.save(curso);

    // Criar usuário com curso obrigatório
    Usuario usuario = Usuario.builder()
        .nome("Maria Oliveira")
        .email("maria@email.com")
        .celular("11988888888")
        .dataNascimento(LocalDate.of(1999, 5, 20))
        .curso(curso) // IMPORTANTE!
        .matricula("20250002")
        .tipoUsuario(TipoUsuario.ALUNO)
        .senha("abcdef")
        .build();

    usuarioRepository.save(usuario);

    boolean existe = usuarioRepository.existsByMatricula("20250002");

    assertThat(existe).isTrue();
  }

}
