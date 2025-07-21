package br.ufma.monitoria.service;

import br.ufma.monitoria.model.Disciplina;
import br.ufma.monitoria.repository.DisciplinaRepository;
import br.ufma.monitoria.service.contract.DisciplinaServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DisciplinaServiceTest {

    @Autowired
    private DisciplinaServiceInterface disciplinaService;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Test
    void deveSalvarDisciplinaValida() {
        Disciplina d = Disciplina.builder().nome("Estrutura de Dados").build();
        Disciplina salvo = disciplinaService.salvar(d);

        Assertions.assertNotNull(salvo.getId());
        Assertions.assertEquals("Estrutura de Dados", salvo.getNome());
    }

    @Test
    void deveBuscarDisciplinaPorId() {
        Disciplina d = Disciplina.builder().nome("Matemática Discreta").build();
        Disciplina salvo = disciplinaRepository.save(d);

        Disciplina buscado = disciplinaService.buscarPorId(salvo.getId());
        Assertions.assertEquals("Matemática Discreta", buscado.getNome());
    }

    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        RegraNegocioRuntime erro = Assertions.assertThrows(RegraNegocioRuntime.class, () -> {
            disciplinaService.buscarPorId(999);
        });

        Assertions.assertEquals("Disciplina não encontrada", erro.getMessage());
    }
}
