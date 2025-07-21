package br.ufma.monitoria.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.ufma.monitoria.model.Monitoria;

@SpringBootTest
public class MonitoriaRepositoryTest {

    @Autowired
    MonitoriaRepository monitoriaRepository;

    @Test
    void deveSalvarMonitoriaComTituloEDescricao() {
        Monitoria monitoria = Monitoria.builder()
            .titulo("Monitoria de Estrutura de Dados")
            .descricao("Aulas práticas e exercícios avançados")
            .build();

        Monitoria salvo = monitoriaRepository.save(monitoria);
        assertNotNull(salvo.getId());
        assertEquals("Monitoria de Estrutura de Dados", salvo.getTitulo());
    }
}
