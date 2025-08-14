package br.ufma.monitoria.backend.repository;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.ufma.monitoria.backend.model.Curso;
import br.ufma.monitoria.backend.model.Monitoria;
import br.ufma.monitoria.backend.model.TipoUsuario;
import br.ufma.monitoria.backend.model.Usuario;

@DataJpaTest
class MonitoriaRepositoryTest {

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void deveSalvarEBuscarMonitoriaPorCursoEMonitor() {
        // Criar curso
        Curso curso = Curso.builder()
                .nome("Sistemas de Informação")
                .codigo("SI")
                .build();
        cursoRepository.save(curso);

        // Criar monitor (usuário)
        Usuario monitor = Usuario.builder()
                .nome("João Monitor")
                .email("joao.monitor@email.com")
                .celular("11999999999")
                .dataNascimento(java.time.LocalDate.of(2000, 1, 1))
                .curso(curso)
                .matricula("20250003")
                .tipoUsuario(TipoUsuario.MONITOR)
                .senha("123456")
                .build();
        usuarioRepository.save(monitor);

        // Criar monitoria
        Monitoria monitoria = Monitoria.builder()
                .curso(curso)
                .monitor(monitor)
                .disciplina("Estrutura de Dados")
                .codigoDisciplina("ED01")
                .diasDaSemana("SEGUNDA")
                .horario(LocalTime.of(14, 0))
                .linkSalaVirtual("https://meet.google.com/abc-defg-hij")
                .observacoes("Monitoria introdutória")
                .build();

        monitoriaRepository.save(monitoria);

        // Buscar por curso
        List<Monitoria> porCurso = monitoriaRepository.findByCurso_CursoId(curso.getCursoId());
        assertThat(porCurso).isNotEmpty();
        assertThat(porCurso.get(0).getDisciplina()).isEqualTo("Estrutura de Dados");

        // Buscar por monitor
        List<Monitoria> porMonitor = monitoriaRepository.findByMonitor_UsuarioId(monitor.getUsuarioId());
        assertThat(porMonitor).hasSize(1);

        // Buscar por disciplina (contendo)
        List<Monitoria> porDisciplina = monitoriaRepository.findByDisciplinaContainingIgnoreCase("dados");
        assertThat(porDisciplina).hasSize(1);
    }
}
