package br.ufma.monitoria.backend.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.ufma.monitoria.backend.model.TipoUsuario;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = Usuario.builder()
                .usuarioId(1L)
                .nome("João Teste")
                .email("joao@teste.com")
                .senha("123456")
                .matricula("20250001")
                .tipoUsuario(TipoUsuario.ALUNO)
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .build();
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByMatricula(usuario.getMatricula())).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario salvo = usuarioService.cadastrarUsuario(usuario, null);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("João Teste");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void deveFalharCadastroSeEmailDuplicado() {
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.cadastrarUsuario(usuario, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("E-mail já cadastrado.");
    }

    @Test
    void deveFalharCadastroSeMatriculaDuplicada() {
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByMatricula(usuario.getMatricula())).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.cadastrarUsuario(usuario, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Matrícula já cadastrada.");
    }

    @Test
    void deveFalharCadastroSeTokenMonitorInvalido() {
        usuario.setTipoUsuario(TipoUsuario.MONITOR);

        assertThatThrownBy(() -> usuarioService.cadastrarUsuario(usuario, "TOKEN_ERRADO"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token de monitor inválido.");
    }

    @Test
    void deveLogarUsuarioComSucesso() {
        when(usuarioRepository.findByEmail("joao@teste.com")).thenReturn(Optional.of(usuario));

        Usuario logado = usuarioService.loginComTipo("joao@teste.com", "123456", TipoUsuario.ALUNO);

        assertThat(logado).isNotNull();
        assertThat(logado.getEmail()).isEqualTo("joao@teste.com");
    }

    @Test
    void deveFalharLoginSeSenhaIncorreta() {
        when(usuarioRepository.findByEmail("joao@teste.com")).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> usuarioService.loginComTipo("joao@teste.com", "999999", TipoUsuario.ALUNO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("E-mail ou senha inválidos");
    }

    @Test
    void deveBuscarUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario encontrado = usuarioService.buscarPorId(1L);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getUsuarioId()).isEqualTo(1L);
    }

    @Test
    void deveLancarErroSeUsuarioNaoEncontradoPorId() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorId(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    void deveListarTodosUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario, usuario.toBuilder().usuarioId(2L).email("maria@teste.com").build());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> lista = usuarioService.listarTodos();

        assertThat(lista).hasSize(2);
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        Usuario atualizado = usuario.toBuilder().nome("João Atualizado").build();
        when(usuarioRepository.findById(usuario.getUsuarioId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(atualizado);

        Usuario resultado = usuarioService.atualizarUsuario(atualizado);

        assertThat(resultado.getNome()).isEqualTo("João Atualizado");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deveLancarErroAoAtualizarUsuarioNaoExistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Usuario usuarioFake = Usuario.builder().usuarioId(99L).build();

        assertThatThrownBy(() -> usuarioService.atualizarUsuario(usuarioFake))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    void deveRemoverUsuarioComSucesso() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.removerUsuario(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void deveLancarErroAoRemoverUsuarioNaoExistente() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.removerUsuario(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Usuário não encontrado para exclusão");
    }

    @Test
    void deveVerificarExistenciaPorMatricula() {
        when(usuarioRepository.existsByMatricula("20250001")).thenReturn(true);

        boolean existe = usuarioService.existsByMatricula("20250001");

        assertThat(existe).isTrue();
    }

    @Test
    void deveVerificarExistenciaPorEmail() {
        when(usuarioRepository.existsByEmail("joao@teste.com")).thenReturn(true);

        boolean existe = usuarioService.existsByEmail("joao@teste.com");

        assertThat(existe).isTrue();
    }
}
