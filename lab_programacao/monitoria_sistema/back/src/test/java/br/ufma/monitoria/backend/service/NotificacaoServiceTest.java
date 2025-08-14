package br.ufma.monitoria.backend.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.ufma.monitoria.backend.model.Notificacao;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.repository.NotificacaoRepository;
import jakarta.persistence.EntityNotFoundException;

class NotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private NotificacaoService notificacaoService;

    private Notificacao notificacao;
    private Usuario usuario;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = Usuario.builder()
                .usuarioId(1L)
                .nome("Aluno Teste")
                .build();

        notificacao = Notificacao.builder()
                .notificacaoId(1L)
                .mensagem("Sua sessão foi aprovada!")
                .dataCriacao(LocalDateTime.now())
                .lida(false)
                .usuarioDestino(usuario)
                .build();
    }

    @Test
    void deveRegistrarNotificacao() {
        when(notificacaoRepository.save(notificacao)).thenReturn(notificacao);

        Notificacao salva = notificacaoService.registrarNotificacao(notificacao);

        assertNotNull(salva);
        assertEquals("Sua sessão foi aprovada!", salva.getMensagem());
        verify(notificacaoRepository, times(1)).save(notificacao);
    }

    @Test
    void deveBuscarPorId() {
        when(notificacaoRepository.findById(1L)).thenReturn(Optional.of(notificacao));

        Notificacao encontrada = notificacaoService.buscarPorId(1L);

        assertEquals("Sua sessão foi aprovada!", encontrada.getMensagem());
    }

    @Test
    void deveLancarExcecaoSeIdNaoEncontrado() {
        when(notificacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> notificacaoService.buscarPorId(99L));
    }

    @Test
    void deveListarPorUsuario() {
        when(notificacaoRepository.findByUsuarioDestino_UsuarioId(1L))
                .thenReturn(Arrays.asList(notificacao));

        List<Notificacao> lista = notificacaoService.listarPorUsuario(1L);

        assertEquals(1, lista.size());
        assertFalse(lista.get(0).getLida());
    }

    @Test
    void deveMarcarComoLida() {
        when(notificacaoRepository.findById(1L)).thenReturn(Optional.of(notificacao));
        when(notificacaoRepository.save(any(Notificacao.class))).thenAnswer(i -> i.getArguments()[0]);

        Notificacao lida = notificacaoService.marcarComoLida(1L);

        assertTrue(lida.getLida());
        verify(notificacaoRepository, times(1)).save(lida);
    }

    @Test
    void deveExcluirNotificacao() {
        when(notificacaoRepository.existsById(1L)).thenReturn(true);

        notificacaoService.excluirNotificacao(1L);

        verify(notificacaoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoExcluirNaoExistente() {
        when(notificacaoRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> notificacaoService.excluirNotificacao(99L));
    }
}
