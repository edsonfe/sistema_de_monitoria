package br.ufma.monitoria.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.ufma.monitoria.model.Atividade;

@SpringBootTest
public class AtividadeRepositoryTest {

    @Autowired
    AtividadeRepository atividadeRepository;

    @Test
    void deveSalvarAtividade() {
        Atividade atividade = Atividade.builder()
            .titulo("Lista 1")
            .descricao("Exercícios de introdução")
            .build();

        atividadeRepository.save(atividade);
        assertFalse(atividadeRepository.findAll().isEmpty());
    }
}
