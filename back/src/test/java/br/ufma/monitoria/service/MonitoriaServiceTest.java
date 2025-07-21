package br.ufma.monitoria.service;

import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.model.Disciplina;
import br.ufma.monitoria.model.Curso;
import br.ufma.monitoria.repository.MonitoriaRepository;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.repository.CursoRepository;
import br.ufma.monitoria.repository.DisciplinaRepository;
import br.ufma.monitoria.service.contract.MonitoriaServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MonitoriaServiceTest {

    @Autowired
    private MonitoriaServiceInterface monitoriaService;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Test
    void deveSalvarMonitoriaValida() {
        Curso curso = cursoRepository.save(Curso.builder().nome("Computação").build());
        Disciplina disciplina = disciplinaRepository.save(Disciplina.builder().nome("POO").build());
        Usuario monitor = usuarioRepository.save(Usuario.builder()
            .nome("Bruna").email("bruna@email.com").senha("123").curso(curso).disciplina(disciplina).build());

        Monitoria monitoria = Monitoria.builder()
            .monitor(monitor)
            .disciplina(disciplina)
            .build();

        Monitoria salva = monitoriaService.salvar(monitoria);

        Assertions.assertNotNull(salva.getId());
        Assertions.assertEquals("Bruna", salva.getMonitor().getNome());
    }

    @Test
    void deveBuscarMonitoriaPorId() {
        Curso curso = cursoRepository.save(Curso.builder().nome("Engenharia").build());
        Disciplina disciplina = disciplinaRepository.save(Disciplina.builder().nome("Algoritmos").build());
        Usuario monitor = usuarioRepository.save(Usuario.builder()
            .nome("Luis").email("luis@email.com").senha("456").curso(curso).disciplina(disciplina).build());

        Monitoria monitoria = monitoriaRepository.save(
            Monitoria.builder().monitor(monitor).disciplina(disciplina).build()
        );

        Monitoria buscada = monitoriaService.buscarPorId(monitoria.getId());
        Assertions.assertEquals("Luis", buscada.getMonitor().getNome());
    }

    @Test
    void deveLancarErroAoBuscarMonitoriaInexistente() {
        RegraNegocioRuntime ex = Assertions.assertThrows(RegraNegocioRuntime.class, () -> {
            monitoriaService.buscarPorId(999);
        });

        Assertions.assertEquals("Monitoria não encontrada", ex.getMessage());
    }
}
