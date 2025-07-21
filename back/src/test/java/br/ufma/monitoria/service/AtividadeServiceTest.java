package br.ufma.monitoria.service;

import br.ufma.monitoria.model.*;
import br.ufma.monitoria.repository.*;
import br.ufma.monitoria.service.contract.AtividadeServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class AtividadeServiceTest {

    @Autowired
    private AtividadeServiceInterface atividadeService;

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Test
    void deveSalvarAtividadeValida() {
        Curso curso = cursoRepository.save(Curso.builder().nome("Engenharia de Produção").build());
        Disciplina disciplina = disciplinaRepository.save(Disciplina.builder().nome("Estatística").build());
        Usuario monitor = usuarioRepository.save(Usuario.builder()
            .nome("Clara").email("clara@email.com").senha("clara123")
            .tipo(TipoUsuario.MONITOR).curso(curso).disciplina(disciplina).build());

        Monitoria monitoria = monitoriaRepository.save(Monitoria.builder()
            .monitor(monitor).disciplina(disciplina).build());

        Atividade atividade = Atividade.builder()
            .titulo("Exercícios estatísticos")
            .descricao("Resolva os exercícios do link abaixo.")
            .linkFormulario("https://forms.exemplo.com/estatistica")
            .monitoria(monitoria)
            .build();

        Atividade salva = atividadeService.salvar(atividade);

        assertNotNull(salva.getId());
        assertEquals("Exercícios estatísticos", salva.getTitulo());
        assertEquals("https://forms.exemplo.com/estatistica", salva.getLinkFormulario());
    }

    @Test
    void deveBuscarAtividadePorId() {
        Atividade a = atividadeRepository.save(Atividade.builder()
            .titulo("Atividade de revisão")
            .descricao("Lista de questões")
            .linkFormulario("https://forms.exemplo.com/revisao")
            .build());

        Atividade buscada = atividadeService.buscarPorId(a.getId());

        assertEquals("Atividade de revisão", buscada.getTitulo());
        assertEquals("Lista de questões", buscada.getDescricao());
    }

    @Test
    void deveLancarErroAoBuscarAtividadeInexistente() {
        RegraNegocioRuntime erro = assertThrows(RegraNegocioRuntime.class, () -> {
            atividadeService.buscarPorId(999);
        });

        assertEquals("Atividade não encontrada", erro.getMessage());
    }
}
