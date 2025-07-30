package br.ufma.monitoria.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class StatusSessaoTest {

    @Test
    void deveConterValoresEsperados() {
        assertEquals("PENDENTE", StatusSessao.PENDENTE.name());
        assertEquals("CONCLUIDA", StatusSessao.CONCLUIDA.name());
        assertEquals("CANCELADA", StatusSessao.CANCELADA.name());
        assertEquals("REALIZADA", StatusSessao.REALIZADA.name());
        assertEquals("AGENDADA", StatusSessao.AGENDADA.name());
        
    }

    @Test
    void deveGerarExcecaoParaValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            StatusSessao.valueOf("INEXISTENTE");
        });
    }
}
