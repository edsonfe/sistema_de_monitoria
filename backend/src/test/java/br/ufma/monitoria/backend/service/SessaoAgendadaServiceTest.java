package br.ufma.monitoria.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.ufma.monitoria.backend.model.Curso;
import br.ufma.monitoria.backend.model.Monitoria;
import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.model.StatusSessao;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.repository.SessaoAgendadaRepository;

class SessaoAgendadaServiceTest {

    @Mock
    private SessaoAgendadaRepository sessaoRepository;

    @InjectMocks
    private SessaoAgendadaService sessaoService;

    private SessaoAgendada sessao;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Usuario aluno = Usuario.builder().usuarioId(1L).nome("Maria").build();
        Usuario monitor = Usuario.builder().usuarioId(2L).nome("João").build();
        Curso curso = Curso.builder().cursoId(1L).nome("Computação").codigo("CC001").build();

        Monitoria monitoria = Monitoria.builder()
                .monitoriaId(1L)
                .disciplina("Estrutura de Dados")
                .monitor(monitor)
                .curso(curso)
                .build();

        sessao = SessaoAgendada.builder()
                .sessaoId(1L)
                .aluno(aluno)
                .monitoria(monitoria)
                .data(LocalDateTime.now().plusDays(1))
                .status(StatusSessao.AGUARDANDO_APROVACAO)
                .build();
    }

    @Test
    void deveAgendarSessaoComSucesso() {
        when(sessaoRepository.save(sessao)).thenReturn(sessao);

        SessaoAgendada salva = sessaoService.agendarSessao(sessao);

        assertThat(salva).isNotNull();
        assertThat(salva.getStatus()).isEqualTo(StatusSessao.AGUARDANDO_APROVACAO);
    }

    @Test
    void deveFalharAoAgendarSessaoComDataPassada() {
        sessao.setData(LocalDateTime.now().minusDays(1));

        assertThatThrownBy(() -> sessaoService.agendarSessao(sessao))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A data da sessão deve ser futura");
    }

    @Test
    void deveBuscarSessaoPorId() {
        when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessao));

        SessaoAgendada encontrada = sessaoService.buscarPorId(1L);

        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getAluno().getNome()).isEqualTo("Maria");
    }

    @Test
    void deveAtualizarStatusSessao() {
        when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        when(sessaoRepository.save(sessao)).thenReturn(sessao);

        SessaoAgendada atualizada = sessaoService.atualizarStatus(1L, StatusSessao.DEFERIDA);

        assertThat(atualizada.getStatus()).isEqualTo(StatusSessao.DEFERIDA);
    }

    @Test
    void deveCancelarSessaoComSucesso() {
        when(sessaoRepository.existsById(1L)).thenReturn(true);

        sessaoService.cancelarSessao(1L);

        verify(sessaoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveFalharCancelamentoSessaoInexistente() {
        when(sessaoRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> sessaoService.cancelarSessao(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Sessão não encontrada para cancelamento");
    }
}
