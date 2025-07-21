package br.ufma.monitoria.service;

import br.ufma.monitoria.model.*;
import br.ufma.monitoria.repository.*;
import br.ufma.monitoria.service.contract.ComentarioServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class ComentarioServiceTest {

    @Autowired
    private ComentarioServiceInterface comentarioService;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Test
    void deveSalvarComentarioValido() {
        Curso curso = cursoRepository.save(Curso.builder().nome("Design Gráfico").build());
        Disciplina disciplina = disciplinaRepository.save(Disciplina.builder().nome("Tipografia").build());
        Usuario autor = usuarioRepository.save(Usuario.builder()
            .nome("Daniela").email("daniela@email.com").senha("1234")
            .tipo(TipoUsuario.ALUNO).curso(curso).disciplina(disciplina).build());
        Monitoria monitoria = monitoriaRepository.save(Monitoria.builder()
            .monitor(autor).disciplina(disciplina).build());

        Comentario comentario = Comentario.builder()
            .texto("Ótima sessão sobre hierarquia visual.")
            .nota(5)
            .data(LocalDateTime.now())
            .autor(autor)
            .monitoria(monitoria)
            .build();

        Comentario salvo = comentarioService.salvar(comentario);

        assertNotNull(salvo.getId());
        assertEquals("Ótima sessão sobre hierarquia visual.", salvo.getTexto());
        assertEquals(5, salvo.getNota());
    }

    @Test
    void deveBuscarComentarioPorId() {
        Comentario c = comentarioRepository.save(Comentario.builder()
            .texto("Comentário de teste")
            .nota(4)
            .data(LocalDateTime.now())
            .build());

        Comentario buscado = comentarioService.buscarPorId(c.getId());

        assertEquals("Comentário de teste", buscado.getTexto());
        assertEquals(4, buscado.getNota());
    }

    @Test
    void deveLancarErroAoBuscarComentarioInexistente() {
        RegraNegocioRuntime erro = assertThrows(RegraNegocioRuntime.class, () -> {
            comentarioService.buscarPorId(999);
        });

        assertEquals("Comentário não encontrado", erro.getMessage());
    }
}
