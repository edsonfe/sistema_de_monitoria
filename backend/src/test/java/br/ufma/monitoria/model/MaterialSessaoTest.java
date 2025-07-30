package br.ufma.monitoria.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class MaterialSessaoTest {

    @Test
    void deveCriarMaterialSessaoComDadosValidos() {
        SessaoMonitoria sessao = SessaoMonitoria.builder()
            .id(UUID.randomUUID())
            .monitoria(Monitoria.builder().id(UUID.randomUUID()).codigo("MON100").curso("Ciência da Computação").build())
            .aluno(Usuario.builder().id(UUID.randomUUID()).nome("Paulo").email("paulo@teste.com").senha("123").tipo(TipoUsuario.ALUNO).build())
            .dataHora(LocalDateTime.of(2025, 7, 26, 10, 0))
            .status(StatusSessao.CONCLUIDA)
            .build();

        MaterialSessao material = MaterialSessao.builder()
            .id(UUID.randomUUID())
            .titulo("Exercícios resolvidos")
            .link("https://material.exemplo.com/exercicios")
            .arquivo("exercicios.pdf")
            .sessaoMonitoria(sessao)
            .dataEnvio(LocalDate.of(2025, 7, 26))
            .build();

        assertNotNull(material.getId());
        assertEquals("Exercícios resolvidos", material.getTitulo());
        assertTrue(material.getLink().startsWith("https://"));
        assertEquals("exercicios.pdf", material.getArquivo());
        assertEquals(sessao, material.getSessaoMonitoria());
        assertEquals(LocalDate.of(2025, 7, 26), material.getDataEnvio());
    }

    @Test
    void devePermitirLinkOuArquivoNulos() {
        MaterialSessao material = MaterialSessao.builder()
            .titulo("Resumo sem material")
            .sessaoMonitoria(SessaoMonitoria.builder().id(UUID.randomUUID()).build())
            .dataEnvio(LocalDate.now())
            .build();

        assertNull(material.getLink());
        assertNull(material.getArquivo());
        assertEquals("Resumo sem material", material.getTitulo());
    }
}
