package br.ufma.monitoria.backend.repository;

import br.ufma.monitoria.backend.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SessaoAgendadaRepositoryTest {

    @Autowired
    private SessaoAgendadaRepository sessaoAgendadaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void deveSalvarEBuscarSessoesPorAlunoEMonitor() {
        // Criar curso
        Curso curso = Curso.builder()
                .nome("Engenharia de Computação")
                .codigo("ENGCOMP")
                .build();
        cursoRepository.save(curso);

        // Criar aluno
        Usuario aluno = Usuario.builder()
                .nome("Aluno Teste")
                .email("aluno@teste.com")
                .celular("999999999")
                .dataNascimento(java.time.LocalDate.of(2000, 1, 1))
                .curso(curso)
                .matricula("20251234")
                .tipoUsuario(TipoUsuario.ALUNO)
                .senha("senha123")
                .build();
        usuarioRepository.save(aluno);

        // Criar monitor
        Usuario monitor = Usuario.builder()
                .nome("Monitor Teste")
                .email("monitor@teste.com")
                .celular("888888888")
                .dataNascimento(java.time.LocalDate.of(1995, 5, 5))
                .curso(curso)
                .matricula("20250001")
                .tipoUsuario(TipoUsuario.MONITOR)
                .senha("senha123")
                .build();
        usuarioRepository.save(monitor);

        // Criar monitoria
        Monitoria monitoria = Monitoria.builder()
                .curso(curso)
                .monitor(monitor)
                .disciplina("Matemática Discreta")
                .codigoDisciplina("MD01")
                .diasDaSemana("TERÇA")
                .horario(java.time.LocalTime.of(10, 0))
                .linkSalaVirtual("https://meet.google.com/xyz-abc")
                .observacoes("Sessão de matemática")
                .build();
        monitoriaRepository.save(monitoria);

        // Criar sessão agendada
        SessaoAgendada sessao = SessaoAgendada.builder()
                .aluno(aluno)
                .monitoria(monitoria)
                .data(LocalDateTime.now().plusDays(1))
                .status(StatusSessao.AGUARDANDO_APROVACAO)
                .build();
        sessaoAgendadaRepository.save(sessao);

        // Testar busca por aluno
        List<SessaoAgendada> sessoesAluno = sessaoAgendadaRepository.findByAluno_UsuarioId(aluno.getUsuarioId());
        assertThat(sessoesAluno).hasSize(1);

        // Testar busca por monitor
        List<SessaoAgendada> sessoesMonitor = sessaoAgendadaRepository.findByMonitoria_Monitor_UsuarioId(monitor.getUsuarioId());
        assertThat(sessoesMonitor).hasSize(1);

        // Testar busca por status
        List<SessaoAgendada> sessoesStatus = sessaoAgendadaRepository.findByStatus(StatusSessao.AGUARDANDO_APROVACAO);
        assertThat(sessoesStatus).hasSize(1);

        // Testar busca de sessões futuras por aluno
        List<SessaoAgendada> sessoesFuturasAluno = sessaoAgendadaRepository.findByAluno_UsuarioIdAndDataAfter(aluno.getUsuarioId(), LocalDateTime.now());
        assertThat(sessoesFuturasAluno).hasSize(1);

        // Testar busca de sessões futuras por monitor
        List<SessaoAgendada> sessoesFuturasMonitor = sessaoAgendadaRepository.findByMonitoria_Monitor_UsuarioIdAndDataAfter(monitor.getUsuarioId(), LocalDateTime.now());
        assertThat(sessoesFuturasMonitor).hasSize(1);
    }
}
