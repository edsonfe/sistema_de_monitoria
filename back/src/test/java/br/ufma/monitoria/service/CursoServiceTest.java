package br.ufma.monitoria.service;

import br.ufma.monitoria.model.Curso;
import br.ufma.monitoria.repository.CursoRepository;
import br.ufma.monitoria.service.contract.CursoServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CursoServiceTest {

    @Autowired
    private CursoServiceInterface cursoService;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void deveSalvarCursoValido() {
        Curso c = Curso.builder().nome("Engenharia de Software").build();
        Curso salvo = cursoService.salvar(c);

        Assertions.assertNotNull(salvo.getId());
        Assertions.assertEquals("Engenharia de Software", salvo.getNome());
    }

    @Test
    void deveBuscarCursoPorId() {
        Curso c = Curso.builder().nome("Arquitetura").build();
        Curso salvo = cursoRepository.save(c);

        Curso encontrado = cursoService.buscarPorId(salvo.getId());
        Assertions.assertEquals("Arquitetura", encontrado.getNome());
    }

    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        RegraNegocioRuntime ex = Assertions.assertThrows(RegraNegocioRuntime.class, () -> {
            cursoService.buscarPorId(999);
        });

        Assertions.assertEquals("Curso não encontrado", ex.getMessage());
    }
}
