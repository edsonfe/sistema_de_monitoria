package br.ufma.monitoria.model;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class MonitoriaTest {

    @Test
    void deveCriarMonitoriaComCamposObrigatorios() {
        Usuario monitor = Usuario.builder().id(UUID.randomUUID()).nome("Ana").build();

        Monitoria monitoria = Monitoria.builder()
            .id(UUID.randomUUID())
            .monitor(monitor)
            .codigo("MON123")
            .curso("Engenharia da Computação")
            .build();

        assertNotNull(monitoria.getId());
        assertEquals("MON123", monitoria.getCodigo());
        assertEquals("Engenharia da Computação", monitoria.getCurso());
        assertEquals("Ana", monitoria.getMonitor().getNome());
        assertNull(monitoria.getDisciplina()); // opcional
        assertNull(monitoria.getLink()); // opcional
        assertNull(monitoria.getObservacoes()); // opcional
    }

    @Test
    void listasDevemIniciarVaziasOuNulas() {
        Monitoria monitoria = Monitoria.builder()
            .monitor(Usuario.builder().id(UUID.randomUUID()).build())
            .codigo("MON456")
            .curso("Matemática")
            .build();

        // Verifica se as listas estão nulas ou vazias
        assertTrue(monitoria.getAgendamentos() == null || monitoria.getAgendamentos().isEmpty());
        assertTrue(monitoria.getSessoes() == null || monitoria.getSessoes().isEmpty());
        assertTrue(monitoria.getMateriais() == null || monitoria.getMateriais().isEmpty());
    }
}
