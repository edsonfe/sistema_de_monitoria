package br.ufma.monitoria.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import br.ufma.monitoria.model.*;
import br.ufma.monitoria.repository.MensagemChatRepository;
import br.ufma.monitoria.service.exceptions.MensagemVaziaException;
import br.ufma.monitoria.service.exceptions.SessaoMonitoriaNotFoundException;
import br.ufma.monitoria.service.exceptions.UsuarioNotFoundException;
import br.ufma.monitoria.service.impl.MensagemChatService;

class MensagemChatServiceImplTest {

    @Mock
    private MensagemChatRepository mensagemRepository;

    @InjectMocks
    private MensagemChatService mensagemService;

    private UUID sessaoId;
    private SessaoMonitoria sessao;
    private Usuario autor;
    private MensagemChat mensagem;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        MockitoAnnotations.openMocks(this);
        sessaoId = UUID.randomUUID();
        sessao = SessaoMonitoria.builder().id(sessaoId).build();
        autor = Usuario.builder().id(UUID.randomUUID()).nome("Aluno").build();

        mensagem = MensagemChat.builder()
                .id(UUID.randomUUID())
                .sessaoMonitoria(sessao)
                .autor(autor)
                .conteudo("Olá, posso tirar dúvida sobre grafos?")
                .dataHora(LocalDateTime.now())
                .build();
    }

    @Test
    void testSalvarMensagemComSucesso() {
        when(mensagemRepository.save(any())).thenReturn(mensagem);

        MensagemChat resultado = mensagemService.salvarMensagem(mensagem);

        assertNotNull(resultado);
        assertEquals("Aluno", resultado.getAutor().getNome());
        verify(mensagemRepository).save(mensagem);
    }

    @Test
    void testSalvarMensagemVazia() {
        mensagem.setConteudo("");

        assertThrows(MensagemVaziaException.class, () -> {
            mensagemService.salvarMensagem(mensagem);
        });
    }

    @Test
    void testSalvarMensagemSemAutor() {
        mensagem.setAutor(null);

        assertThrows(UsuarioNotFoundException.class, () -> {
            mensagemService.salvarMensagem(mensagem);
        });
    }

    @Test
    void testSalvarMensagemSemSessao() {
        mensagem.setSessaoMonitoria(null);

        assertThrows(SessaoMonitoriaNotFoundException.class, () -> {
            mensagemService.salvarMensagem(mensagem);
        });
    }

    @Test
    void testListarMensagensPorSessaoComMensagens() {
        when(mensagemRepository.findBySessaoMonitoriaIdOrderByDataHoraAsc(sessaoId))
                .thenReturn(List.of(mensagem));

        List<MensagemChat> mensagens = mensagemService.listarMensagensPorSessao(sessaoId);

        assertEquals(1, mensagens.size());
        assertEquals(sessaoId, mensagens.get(0).getSessaoMonitoria().getId());
        verify(mensagemRepository).findBySessaoMonitoriaIdOrderByDataHoraAsc(sessaoId);
    }

    @Test
    void testListarMensagensPorSessaoSemMensagens() {
        when(mensagemRepository.findBySessaoMonitoriaIdOrderByDataHoraAsc(sessaoId))
                .thenReturn(Collections.emptyList());

        assertThrows(MensagemVaziaException.class, () -> {
            mensagemService.listarMensagensPorSessao(sessaoId);
        });
    }
}
