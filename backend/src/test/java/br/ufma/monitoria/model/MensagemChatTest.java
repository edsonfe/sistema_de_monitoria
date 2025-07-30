package br.ufma.monitoria.model;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class MensagemChatTest {

    @Test
    void deveCriarMensagemChatComDadosValidos() {
        Usuario autor = Usuario.builder()
            .id(UUID.randomUUID())
            .nome("Lucas")
            .email("lucas@exemplo.com")
            .senha("1234")
            .tipo(TipoUsuario.ALUNO)
            .build();

        SessaoMonitoria sessao = SessaoMonitoria.builder()
            .id(UUID.randomUUID())
            .dataHora(LocalDateTime.now())
            .monitoria(Monitoria.builder().id(UUID.randomUUID()).build())
            .aluno(autor)
            .status(StatusSessao.CONCLUIDA)
            .build();

        MensagemChat mensagem = MensagemChat.builder()
            .id(UUID.randomUUID())
            .conteudo("Boa tarde, podemos revisar os exercícios?")
            .dataHora(LocalDateTime.of(2025, 7, 26, 14, 30))
            .autor(autor)
            .sessaoMonitoria(sessao)
            .build();

        assertNotNull(mensagem.getId());
        assertEquals("Boa tarde, podemos revisar os exercícios?", mensagem.getConteudo());
        assertEquals(LocalDateTime.of(2025, 7, 26, 14, 30), mensagem.getDataHora());
        assertEquals(autor.getNome(), mensagem.getAutor().getNome());
        assertEquals(sessao.getId(), mensagem.getSessaoMonitoria().getId());
    }

    @Test
    void deveGerarExcecaoSeConteudoForNulo() {
        assertThrows(NullPointerException.class, () -> {
            MensagemChat.builder()
                .dataHora(LocalDateTime.now())
                .autor(Usuario.builder().id(UUID.randomUUID()).build())
                .sessaoMonitoria(SessaoMonitoria.builder().id(UUID.randomUUID()).build())
                .build()
                .getConteudo().length(); // força acesso nulo
        });
    }
}
