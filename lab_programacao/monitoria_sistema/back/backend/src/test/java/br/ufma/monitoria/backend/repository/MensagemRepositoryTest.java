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
class MensagemRepositoryTest {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private SessaoAgendadaRepository sessaoAgendadaRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void deveSalvarEBuscarMensagensPorSessao() {
        // ---------- 1. Criar Curso ----------
        Curso curso = Curso.builder()
                .nome("Engenharia de Software")
                .codigo("ES01")
                .build();
        cursoRepository.save(curso);

        // ---------- 2. Criar Monitor ----------
        Usuario monitor = Usuario.builder()
                .nome("Monitor Mensagem")
                .email("monitor.msg@teste.com")
                .senha("123456")
                .celular("111111111")
                .dataNascimento(LocalDate.of(1994, 3, 10))
                .curso(curso)
                .matricula("20250006")
                .tipoUsuario(TipoUsuario.MONITOR)
                .build();
        usuarioRepository.save(monitor);

        // ---------- 3. Criar Aluno ----------
        Usuario aluno = Usuario.builder()
                .nome("Aluno Mensagem")
                .email("aluno.msg@teste.com")
                .senha("123456")
                .celular("222222222")
                .dataNascimento(LocalDate.of(2001, 7, 15))
                .curso(curso)
                .matricula("20250007")
                .tipoUsuario(TipoUsuario.ALUNO)
                .build();
        usuarioRepository.save(aluno);

        // ---------- 4. Criar Monitoria ----------
        Monitoria monitoria = Monitoria.builder()
                .curso(curso)
                .monitor(monitor)
                .disciplina("Estrutura de Dados")
                .codigoDisciplina("ED01")
                .diasDaSemana("TERÇA")
                .horario(LocalTime.of(14, 0))
                .linkSalaVirtual("https://meet.google.com/msg-ed01")
                .observacoes("Sessão inicial de ED")
                .build();
        monitoriaRepository.save(monitoria);

        // ---------- 5. Criar Sessão ----------
        SessaoAgendada sessao = SessaoAgendada.builder()
                .aluno(aluno)
                .monitoria(monitoria)
                .data(LocalDateTime.now().plusDays(1))
                .status(StatusSessao.AGUARDANDO_APROVACAO)
                .build();
        sessaoAgendadaRepository.save(sessao);

        // ---------- 6. Criar Mensagem ----------
        Mensagem mensagem = Mensagem.builder()
                .sessaoAgendada(sessao)
                .autor(aluno)
                .conteudo("Olá, qual material devo estudar antes da sessão?")
                .dataHora(LocalDateTime.now())
                .build();
        mensagemRepository.save(mensagem);

        // ---------- 7. Buscar mensagens por sessão ----------
        List<Mensagem> mensagens = mensagemRepository.findBySessaoAgendada_SessaoIdOrderByDataHoraAsc(sessao.getSessaoId());

        assertThat(mensagens).hasSize(1);
        assertThat(mensagens.get(0).getConteudo())
                .isEqualTo("Olá, qual material devo estudar antes da sessão?");
        assertThat(mensagens.get(0).getAutor().getNome())
                .isEqualTo("Aluno Mensagem");
    }
}
