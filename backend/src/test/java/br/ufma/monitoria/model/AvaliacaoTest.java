package br.ufma.monitoria.model;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class AvaliacaoTest {

    @Test
    void deveCriarAvaliacaoComCamposObrigatorios() {
        SessaoMonitoria sessao = SessaoMonitoria.builder()
            .id(UUID.randomUUID())
            .monitoria(Monitoria.builder().id(UUID.randomUUID()).codigo("MON200").curso("Engenharia Elétrica").build())
            .aluno(Usuario.builder().id(UUID.randomUUID()).nome("Julia").email("julia@exemplo.com").senha("segura").tipo(TipoUsuario.ALUNO).build())
            .dataHora(LocalDateTime.of(2025, 7, 26, 15, 0))
            .status(StatusSessao.CONCLUIDA)
            .build();

        Avaliacao avaliacao = Avaliacao.builder()
            .id(UUID.randomUUID())
            .sessaoMonitoria(sessao)
            .nota(5)
            .comentario("Explicação excelente e paciente.")
            .data(LocalDateTime.of(2025, 7, 26, 16, 0))
            .build();

        assertNotNull(avaliacao.getId());
        assertEquals(5, avaliacao.getNota());
        assertEquals("Explicação excelente e paciente.", avaliacao.getComentario());
        assertEquals(sessao.getId(), avaliacao.getSessaoMonitoria().getId());
        assertEquals(LocalDateTime.of(2025, 7, 26, 16, 0), avaliacao.getData());
    }

    @Test
    void devePermitirComentarioNulo() {
        Avaliacao avaliacao = Avaliacao.builder()
            .sessaoMonitoria(SessaoMonitoria.builder().id(UUID.randomUUID()).build())
            .nota(3)
            .data(LocalDateTime.now())
            .build();

        assertNull(avaliacao.getComentario());
    }
}
