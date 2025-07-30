package br.ufma.monitoria.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TipoUsuarioTest {

    @Test
    void deveConterValoresEsperados() {
        assertEquals("ALUNO", TipoUsuario.ALUNO.name());
        assertEquals("MONITOR", TipoUsuario.MONITOR.name());
        assertEquals("ADMIN", TipoUsuario.ADMIN.name());
    }

    @Test
    void deveGerarExcecaoParaTipoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            TipoUsuario.valueOf("COORDENADOR"); // não existe no enum atual
        });
    }

    @Test
    void devePermitirComparacaoEntreEnums() {
        TipoUsuario tipo = TipoUsuario.valueOf("MONITOR");
        assertTrue(tipo == TipoUsuario.MONITOR);
        assertNotEquals(TipoUsuario.ALUNO, tipo);
    }
}
