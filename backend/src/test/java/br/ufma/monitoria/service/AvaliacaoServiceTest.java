package br.ufma.monitoria.service;

import br.ufma.monitoria.model.Avaliacao;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.repository.AvaliacaoRepository;
import br.ufma.monitoria.service.impl.AvaliacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvaliacaoServiceTest {

    private AvaliacaoRepository avaliacaoRepository;
    private AvaliacaoService avaliacaoService;

    @BeforeEach
    void setUp() {
        avaliacaoRepository = mock(AvaliacaoRepository.class);
        avaliacaoService = new AvaliacaoService(avaliacaoRepository);
    }

    @Test
    void deveSalvarAvaliacao() {
        Avaliacao avaliacao = Avaliacao.builder()
            .nota(5)
            .comentario("Muito boa")
            .data(LocalDateTime.now())
            .build();

        when(avaliacaoRepository.save(avaliacao)).thenReturn(avaliacao);

        Avaliacao resultado = avaliacaoService.salvarAvaliacao(avaliacao);

        assertNotNull(resultado);
        assertEquals(5, resultado.getNota());
    }

    @Test
    void deveCalcularMediaPorMonitoria() {
        UUID monitoriaId = UUID.randomUUID();

        Monitoria monitoria = new Monitoria();
        monitoria.setId(monitoriaId);

        SessaoMonitoria sessao1 = new SessaoMonitoria();
        sessao1.setMonitoria(monitoria);

        SessaoMonitoria sessao2 = new SessaoMonitoria();
        sessao2.setMonitoria(monitoria);

        Avaliacao a1 = Avaliacao.builder().sessaoMonitoria(sessao1).nota(4).build();
        Avaliacao a2 = Avaliacao.builder().sessaoMonitoria(sessao2).nota(2).build();

        when(avaliacaoRepository.findAll()).thenReturn(List.of(a1, a2));

        Double media = avaliacaoService.calcularMediaPorMonitoria(monitoriaId);

        assertEquals(3.0, media);
    }
}
