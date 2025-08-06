package br.ufma.monitoria.backend.repository;

import br.ufma.monitoria.backend.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MaterialRepositoryTest {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SessaoAgendadaRepository sessaoAgendadaRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void deveSalvarEBuscarMateriaisPorSessao() {
        // ---------- 1. Criar Curso ----------
        Curso curso = Curso.builder()
                .nome("Ciência da Computação")
                .codigo("CC01")
                .build();
        cursoRepository.save(curso);

        // ---------- 2. Criar Monitor ----------
        Usuario monitor = Usuario.builder()
                .nome("Monitor Material")
                .email("monitor.material@teste.com")
                .senha("123456")
                .celular("111111111")
                .dataNascimento(LocalDate.of(1995, 1, 1))
                .curso(curso)
                .matricula("20250004")
                .tipoUsuario(TipoUsuario.MONITOR)
                .build();
        usuarioRepository.save(monitor);

        // ---------- 3. Criar Aluno ----------
        Usuario aluno = Usuario.builder()
                .nome("Aluno Material")
                .email("aluno.material@teste.com")
                .senha("123456")
                .celular("222222222")
                .dataNascimento(LocalDate.of(2000, 5, 10))
                .curso(curso)
                .matricula("20250005")
                .tipoUsuario(TipoUsuario.ALUNO)
                .build();
        usuarioRepository.save(aluno);

        // ---------- 4. Criar Monitoria ----------
        Monitoria monitoria = Monitoria.builder()
                .curso(curso)
                .monitor(monitor)
                .disciplina("Banco de Dados")
                .codigoDisciplina("BD01")
                .diasDaSemana("QUARTA")
                .horario(LocalTime.of(16, 0))
                .linkSalaVirtual("https://meet.google.com/mat-bd01")
                .observacoes("Primeira sessão de BD")
                .build();
        monitoriaRepository.save(monitoria);

        // ---------- 5. Criar Sessão ----------
        SessaoAgendada sessao = SessaoAgendada.builder()
                .aluno(aluno)
                .monitoria(monitoria)
                .data(LocalDateTime.now().plusHours(1))
                .status(StatusSessao.AGUARDANDO_APROVACAO)
                .build();
        sessaoAgendadaRepository.save(sessao);

        // ---------- 6. Criar Material ----------
        Material material = Material.builder()
                .sessaoAgendada(sessao)
                .titulo("Apostila de SQL")
                .arquivo("apostila-sql.pdf")
                .linkExterno("https://materiais.com/sql.pdf")
                .dataEnvio(LocalDateTime.now())
                .build();
        materialRepository.save(material);

        // ---------- 7. Buscar materiais por sessão ----------
        List<Material> materiais = materialRepository.findBySessaoAgendada_SessaoId(sessao.getSessaoId());

        assertThat(materiais).hasSize(1);
        assertThat(materiais.get(0).getTitulo()).isEqualTo("Apostila de SQL");
    }
}
