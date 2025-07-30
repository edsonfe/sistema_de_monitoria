package br.ufma.monitoria.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.service.exceptions.BusinessException;
import br.ufma.monitoria.service.impl.UsuarioService;

@DataJpaTest
@Import(TestEncoder.class) // Importa encoder customizado para teste
class UsuarioServiceIntegrationTest {

    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Usando bean externo ao invés de classe anônima

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder);
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        Usuario usuario = Usuario.builder()
            .nome("Edson Aluno")
            .email("edson@ufma.br")
            .matricula("202501234")
            .cpf("000.000.000-00")
            .curso("Computação")
            .senha("senha123")
            .dataNascimento(LocalDate.of(2000, 4, 15))
            .telefone("98991234567")
            .tipo(TipoUsuario.ALUNO)
            .build();

        Usuario salvo = usuarioService.criarUsuario(usuario);

        assertNotNull(salvo.getId());
        assertEquals("Edson Aluno", salvo.getNome());
        assertEquals("98991234567", salvo.getTelefone());
        assertEquals(LocalDate.of(2000, 4, 15), salvo.getDataNascimento());
    }

    @Test
    void deveLancarErroAoCriarMonitorSemCodigo() {
        Usuario monitor = Usuario.builder()
            .nome("Monitor Sem Código")
            .email("monitor@ufma.br")
            .senha("123")
            .tipo(TipoUsuario.MONITOR)
            .build();

        BusinessException ex = assertThrows(BusinessException.class, () ->
            usuarioService.criarUsuario(monitor)
        );

        assertEquals("Monitores precisam de código de verificação.", ex.getMessage());
    }

    @Test
    void deveBuscarPorEmailNoBanco() {
        Usuario usuario = Usuario.builder()
            .nome("Joana")
            .email("joana@ufma.br")
            .senha("123456")
            .tipo(TipoUsuario.ALUNO)
            .dataNascimento(LocalDate.of(1999, 10, 30))
            .telefone("98991002000")
            .build();

        usuarioRepository.save(usuario);

        Optional<Usuario> encontrado = usuarioService.buscarPorEmail("joana@ufma.br");

        assertTrue(encontrado.isPresent());
        assertEquals("Joana", encontrado.get().getNome());
        assertEquals("98991002000", encontrado.get().getTelefone());
        assertEquals(LocalDate.of(1999, 10, 30), encontrado.get().getDataNascimento());
    }

    @Test
    void deveListarUsuariosPorTipo() {
        Usuario aluno1 = Usuario.builder()
            .nome("Carlos")
            .email("carlos@ufma.br")
            .senha("123")
            .telefone("98999991111")
            .dataNascimento(LocalDate.of(2001, 2, 28))
            .tipo(TipoUsuario.ALUNO)
            .build();

        Usuario aluno2 = Usuario.builder()
            .nome("Renata")
            .email("renata@ufma.br")
            .senha("456")
            .telefone("98988882222")
            .dataNascimento(LocalDate.of(2002, 7, 15))
            .tipo(TipoUsuario.ALUNO)
            .build();

        usuarioRepository.saveAll(List.of(aluno1, aluno2));

        List<Usuario> resultado = usuarioService.listarPorTipo(TipoUsuario.ALUNO);

        assertEquals(2, resultado.size());
    }
}
