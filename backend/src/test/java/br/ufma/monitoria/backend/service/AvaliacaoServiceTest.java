package br.ufma.monitoria.backend.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.ufma.monitoria.backend.model.Avaliacao;
import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.repository.AvaliacaoRepository;
import jakarta.persistence.EntityNotFoundException;

class AvaliacaoServiceTest {

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @InjectMocks
    private AvaliacaoService avaliacaoService;

    private Avaliacao avaliacao;
    private Usuario aluno;
    private SessaoAgendada sessao;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        aluno = Usuario.builder()
                .usuarioId(1L)
                .nome("Aluno Teste")
                .build();

        sessao = SessaoAgendada.builder()
                .sessaoId(1L)
                .build();

        avaliacao = Avaliacao.builder()
                .avaliacaoId(1L)
                .estrelas(5)
                .comentario("Ótima sessão")
                .aluno(aluno)
                .sessaoAgendada(sessao)
                .build();
    }

    @Test
    void deveRegistrarAvaliacao() {
        when(avaliacaoRepository.save(avaliacao)).thenReturn(avaliacao);

        Avaliacao salva = avaliacaoService.registrarAvaliacao(avaliacao);

        assertNotNull(salva);
        assertEquals(5, salva.getEstrelas());
        verify(avaliacaoRepository, times(1)).save(avaliacao);
    }

    @Test
    void deveBuscarPorId() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));

        Avaliacao encontrada = avaliacaoService.buscarPorId(1L);

        assertNotNull(encontrada);
        assertEquals("Ótima sessão", encontrada.getComentario());
    }

    @Test
    void deveLancarExcecaoSeIdNaoEncontrado() {
        when(avaliacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> avaliacaoService.buscarPorId(99L));
    }

    @Test
    void deveListarPorSessao() {
        when(avaliacaoRepository.findBySessaoAgendada_SessaoId(1L))
                .thenReturn(Arrays.asList(avaliacao));

        List<Avaliacao> lista = avaliacaoService.listarPorSessao(1L);

        assertEquals(1, lista.size());
        assertEquals(5, lista.get(0).getEstrelas());
    }

    @Test
    void deveListarPorAluno() {
        when(avaliacaoRepository.findByAluno_UsuarioId(1L))
                .thenReturn(Arrays.asList(avaliacao));

        List<Avaliacao> lista = avaliacaoService.listarPorAluno(1L);

        assertEquals(1, lista.size());
        assertEquals("Ótima sessão", lista.get(0).getComentario());
    }

    @Test
    void deveCalcularMediaEstrelasPorSessao() {
        Avaliacao avaliacao2 = Avaliacao.builder()
                .avaliacaoId(2L)
                .estrelas(3)
                .aluno(aluno)
                .sessaoAgendada(sessao)
                .build();

        when(avaliacaoRepository.findBySessaoAgendada_SessaoIdAndEstrelasNotNull(1L))
                .thenReturn(Arrays.asList(avaliacao, avaliacao2));

        double media = avaliacaoService.calcularMediaEstrelasPorSessao(1L);

        assertEquals(4.0, media); // (5+3)/2 = 4.0
    }

    @Test
    void deveExcluirAvaliacao() {
        when(avaliacaoRepository.existsById(1L)).thenReturn(true);

        avaliacaoService.excluirAvaliacao(1L);

        verify(avaliacaoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoExcluirNaoExistente() {
        when(avaliacaoRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> avaliacaoService.excluirAvaliacao(99L));
    }
}
