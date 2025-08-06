package br.ufma.monitoria.backend.repository;

import br.ufma.monitoria.backend.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AvaliacaoRepositoryTest {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private SessaoAgendadaRepository sessaoAgendadaRepository;

    @Test
    void deveSalvarEBuscarAvaliacaoPorSessao() {
        // ---------- 1. Criar Curso ----------
        Curso curso = Curso.builder()
                .nome("Ciência da Computação")
                .codigo("CC01")
                .build();
        cursoRepository.save(curso);

        // ---------- 2. Criar Aluno ----------
        Usuario aluno = Usuario.builder()
                .nome("Aluno Avaliador")
                .email("aluno.avaliador@teste.com")
                .senha("123456")
                .celular("11988887777")
                .dataNascimento(LocalDate.of(2000, 3, 15))
                .curso(curso)
                .matricula("20250009")
                .tipoUsuario(TipoUsuario.ALUNO)
                .build();
        usuarioRepository.save(aluno);

        // ---------- 3. Criar Monitor ----------
        Usuario monitor = Usuario.builder()
                .nome("Monitor Avaliado")
                .email("monitor.avaliado@teste.com")
                .senha("654321")
                .celular("11977776666")
                .dataNascimento(LocalDate.of(1999, 7, 10))
                .curso(curso)
                .matricula("M20250010")
                .tipoUsuario(TipoUsuario.MONITOR)
                .build();
        usuarioRepository.save(monitor);

        // ---------- 4. Criar Monitoria ----------
        Monitoria monitoria = Monitoria.builder()
                .disciplina("Estrutura de Dados")
                .codigoDisciplina("ED01")
                .diasDaSemana("Segunda, Quarta")
                .horario(java.time.LocalTime.of(10, 0))
                .linkSalaVirtual("https://meet.test/ed01")
                .observacoes("Monitoria de prática de ED")
                .monitor(monitor)
                .curso(curso)
                .build();
        monitoriaRepository.save(monitoria);

        // ---------- 5. Criar Sessão Agendada ----------
        SessaoAgendada sessao = SessaoAgendada.builder()
                .monitoria(monitoria)
                .aluno(aluno)
                .data(LocalDateTime.now().plusDays(1))
                .status(StatusSessao.DEFERIDA)
                .build();
        sessaoAgendadaRepository.save(sessao);

        // ---------- 6. Criar Avaliação ----------
        Avaliacao avaliacao = Avaliacao.builder()
                .sessaoAgendada(sessao)
                .aluno(aluno)
                .estrelas(5)
                .comentario("Ótima explicação do monitor!")
                .build();
        avaliacaoRepository.save(avaliacao);

        // ---------- 7. Buscar Avaliações por Sessão ----------
        List<Avaliacao> avaliacoes = avaliacaoRepository.findBySessaoAgendada_SessaoId(sessao.getSessaoId());

        // ---------- 8. Validações ----------
        assertThat(avaliacoes).hasSize(1);
        assertThat(avaliacoes.get(0).getEstrelas()).isEqualTo(5);
        assertThat(avaliacoes.get(0).getComentario()).isEqualTo("Ótima explicação do monitor!");
        assertThat(avaliacoes.get(0).getAluno().getNome()).isEqualTo("Aluno Avaliador");
    }
}
