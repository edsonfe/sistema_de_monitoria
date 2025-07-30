package br.ufma.monitoria.model;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class MaterialMonitoriaTest {

    @Test
    void deveCriarMaterialMonitoriaComDadosValidos() {
        Monitoria monitoria = Monitoria.builder()
            .id(UUID.randomUUID())
            .codigo("MON001")
            .curso("Sistemas de Informação")
            .build();

        MaterialMonitoria material = MaterialMonitoria.builder()
            .id(UUID.randomUUID())
            .titulo("Lista de Exercícios 1")
            .link("https://drive.exemplo.com/lista1")
            .arquivo("lista1.pdf")
            .monitoria(monitoria)
            .dataEnvio(LocalDate.of(2025, 7, 25))
            .build();

        assertNotNull(material.getId());
        assertEquals("Lista de Exercícios 1", material.getTitulo());
        assertTrue(material.getLink().contains("https://"));
        assertEquals("lista1.pdf", material.getArquivo());
        assertEquals(monitoria, material.getMonitoria());
        assertEquals(LocalDate.of(2025, 7, 25), material.getDataEnvio());
    }

    @Test
    void devePermitirLinkEArquivoNulosOuVazios() {
        MaterialMonitoria material = MaterialMonitoria.builder()
            .titulo("Apresentação da Monitoria")
            .monitoria(Monitoria.builder().id(UUID.randomUUID()).build())
            .dataEnvio(LocalDate.now())
            .build();

        assertNull(material.getLink());
        assertNull(material.getArquivo());
    }
}
