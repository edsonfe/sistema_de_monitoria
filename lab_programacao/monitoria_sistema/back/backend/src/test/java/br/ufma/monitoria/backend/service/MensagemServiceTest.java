package br.ufma.monitoria.backend.service;

import br.ufma.monitoria.backend.model.Mensagem;
import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.repository.MensagemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MensagemServiceTest {

    @Mock
    private MensagemRepository mensagemRepository;

    @InjectMocks
    private MensagemService mensagemService;

    private Mensagem mensagem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Usuario autor = Usuario.builder().usuarioId(1L).build();
        SessaoAgendada sessao = SessaoAgendada.builder().sessaoId(1L).build();

        mensagem = Mensagem.builder()
                .mensagemId(1L)
                .conteudo("Olá, tudo bem?")
                .dataHora(LocalDateTime.now())
                .autor(autor)
                .sessaoAgendada(sessao)
                .build();
    }

    @Test
    void deveEnviarMensagem() {
        when(mensagemRepository.save(any(Mensagem.class))).thenReturn(mensagem);

        Mensagem salva = mensagemService.enviarMensagem(mensagem);

        assertNotNull(salva);
        assertEquals("Olá, tudo bem?", salva.getConteudo());
        verify(mensagemRepository, times(1)).save(mensagem);
    }

    @Test
    void deveBuscarMensagemPorId() {
        when(mensagemRepository.findById(1L)).thenReturn(Optional.of(mensagem));

        Optional<Mensagem> encontrada = mensagemService.buscarPorId(1L);

        assertTrue(encontrada.isPresent());
        assertEquals(1L, encontrada.get().getMensagemId());
    }

    @Test
    void deveDeletarMensagem() {
        doNothing().when(mensagemRepository).deleteById(1L);

        mensagemService.deletarMensagem(1L);

        verify(mensagemRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveBuscarMensagensPorSessao() {
        when(mensagemRepository.findBySessaoAgendada_SessaoIdOrderByDataHoraAsc(1L))
                .thenReturn(List.of(mensagem));

        List<Mensagem> mensagens = mensagemService.buscarMensagensPorSessao(1L);

        assertFalse(mensagens.isEmpty());
        assertEquals(1, mensagens.size());
    }
}
