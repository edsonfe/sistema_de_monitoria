package br.ufma.monitoria.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.ufma.monitoria.model.Disciplina;

@SpringBootTest
public class DisciplinaRepositoryTest {

    @Autowired
    DisciplinaRepository disciplinaRepository;

    @Test
    void deveSalvarDisciplina() {
        Disciplina disciplina = Disciplina.builder().nome("Matemática Discreta").build();
        disciplinaRepository.save(disciplina);

        var lista = disciplinaRepository.findAll();
        assertTrue(lista.stream().anyMatch(d -> d.getNome().equals("Matemática Discreta")));
    }
}
