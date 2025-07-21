package br.ufma.monitoria.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.ufma.monitoria.model.Curso;

@SpringBootTest
public class CursoRepositoryTest {

    @Autowired
    CursoRepository cursoRepository;

    @Test
    void deveSalvarCurso() {
        Curso curso = Curso.builder().nome("Ciência da Computação").build();
        Curso salvo = cursoRepository.save(curso);
        assertNotNull(salvo.getId());
    }
}
