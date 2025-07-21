package br.ufma.monitoria.service;

import br.ufma.monitoria.model.*;
import br.ufma.monitoria.repository.*;
import br.ufma.monitoria.service.contract.SessaoServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class SessaoServiceTest {

    @Autowired
    private SessaoServiceInterface sessaoService;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Test
    void deveSalvarSessaoValida() {
        Curso curso = cursoRepository.save(Curso.builder().nome("Engenharia Elétrica").build());
        Disciplina disciplina = disciplinaRepository.save(Disciplina.builder().nome("Eletrônica Digital").build());
        Usuario aluno = usuarioRepository.save(Usuario.builder()
            .nome("João").email("joao@email.com").senha("abc123")
            .tipo(TipoUsuario.ALUNO).curso(curso).disciplina(disciplina).build());
        Usuario monitor = usuarioRepository.save(Usuario.builder()
            .nome("Bruno").email("bruno@email.com").senha("def456")
            .tipo(TipoUsuario.MONITOR).curso(curso).disciplina(disciplina).build());

        Monitoria monitoria = monitoriaRepository.save(Monitoria.builder()
            .monitor(monitor).disciplina(disciplina).build());

        Sessao sessao = Sessao.builder()
            .horario(LocalDateTime.now().plusDays(1))
            .link("https://meet.exemplo.com/sessao1")
            .status("agendada")
            .monitoria(monitoria)
            .aluno(aluno)
            .build();

        Sessao salva = sessaoService.salvar(sessao);

        Assertions.assertNotNull(salva.getId());
        Assertions.assertEquals("agendada", salva.getStatus());
        Assertions.assertEquals("https://meet.exemplo.com/sessao1", salva.getLink());
    }

    @Test
    void deveBuscarSessaoPorId() {
        Curso curso = cursoRepository.save(Curso.builder().nome("Computação").build());
        Disciplina disciplina = disciplinaRepository.save(Disciplina.builder().nome("Estrutura de Dados").build());
        Usuario aluno = usuarioRepository.save(Usuario.builder()
            .nome("Ana").email("ana@email.com").senha("xyz789")
            .tipo(TipoUsuario.ALUNO).curso(curso).disciplina(disciplina).build());
        Usuario monitor = usuarioRepository.save(Usuario.builder()
            .nome("Rafa").email("rafa@email.com").senha("123456")
            .tipo(TipoUsuario.MONITOR).curso(curso).disciplina(disciplina).build());

        Monitoria monitoria = monitoriaRepository.save(Monitoria.builder()
            .monitor(monitor).disciplina(disciplina).build());

        Sessao sessao = sessaoRepository.save(
            Sessao.builder()
                .horario(LocalDateTime.now().plusHours(3))
                .link("https://meet.exemplo.com/sessao2")
                .status("agendada")
                .monitoria(monitoria)
                .aluno(aluno)
                .build()
        );

        Sessao buscada = sessaoService.buscarPorId(sessao.getId());
        Assertions.assertEquals("agendada", buscada.getStatus());
        Assertions.assertEquals("https://meet.exemplo.com/sessao2", buscada.getLink());
    }

    @Test
    void deveLancarErroAoBuscarSessaoInexistente() {
        RegraNegocioRuntime erro = Assertions.assertThrows(RegraNegocioRuntime.class, () -> {
            sessaoService.buscarPorId(999);
        });

        Assertions.assertEquals("Sessão não encontrada", erro.getMessage());
    }
}
