package br.ufma.monitoria.backend.repository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.ufma.monitoria.backend.model.Curso;

@DataJpaTest
class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void deveSalvarEBuscarCursoPorId() {
        // Criar e salvar curso
        Curso curso = Curso.builder()
                .nome("Ciência da Computação")
                .codigo("CCOMP")
                .build();

        Curso salvo = cursoRepository.save(curso);

        // Buscar pelo ID
        Optional<Curso> encontrado = cursoRepository.findById(salvo.getCursoId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Ciência da Computação");
    }

    @Test
    void deveSalvarEBuscarCursoPorCodigo() {
        // Criar e salvar curso
        Curso curso = Curso.builder()
                .nome("Engenharia de Software")
                .codigo("ENGSW")
                .build();

        cursoRepository.save(curso);

        // Buscar por código
        Optional<Curso> encontrado = cursoRepository.findByCodigo("ENGSW");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Engenharia de Software");
    }
}
