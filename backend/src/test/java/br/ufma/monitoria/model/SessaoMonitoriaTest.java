package br.ufma.monitoria.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class SessaoMonitoriaTest {

    @Test
    void deveCriarSessaoMonitoriaComDadosValidos() {
        Monitoria monitoria = Monitoria.builder().id(UUID.randomUUID()).build();
        Usuario aluno = Usuario.builder().id(UUID.randomUUID()).build();

        SessaoMonitoria sessao = SessaoMonitoria.builder()
            .id(UUID.randomUUID())
            .monitoria(monitoria)
            .aluno(aluno)
            .dataHora(LocalDateTime.now())
            .status(StatusSessao.CONCLUIDA)
            .tema("Funções de 2º grau")
            .build();

        assertNotNull(sessao.getId());
        assertEquals("Funções de 2º grau", sessao.getTema());
        assertEquals(StatusSessao.CONCLUIDA, sessao.getStatus());
        assertEquals(monitoria, sessao.getMonitoria());
        assertEquals(aluno, sessao.getAluno());
    }

    @Test
    void temaPodeSerNulo() {
        SessaoMonitoria sessao = SessaoMonitoria.builder()
            .monitoria(Monitoria.builder().id(UUID.randomUUID()).build())
            .aluno(Usuario.builder().id(UUID.randomUUID()).build())
            .dataHora(LocalDateTime.now())
            .status(StatusSessao.PENDENTE)
            .build();

        assertNull(sessao.getTema());
    }
}
