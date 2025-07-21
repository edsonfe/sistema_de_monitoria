package br.ufma.monitoria.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.ufma.monitoria.model.Comentario;

@SpringBootTest
public class ComentarioRepositoryTest {

    @Autowired
    ComentarioRepository comentarioRepository;

    @Test
    void deveSalvarComentario() {
        Comentario comentario = Comentario.builder()
            .texto("Gostei da explicação!")
            .build();

        comentarioRepository.save(comentario);
        assertNotNull(comentario.getId());
    }
}
