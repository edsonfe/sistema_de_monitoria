package br.ufma.monitoria.backend.controller;

import br.ufma.monitoria.backend.model.Avaliacao;
import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.service.AvaliacaoService;
import br.ufma.monitoria.backend.service.SessaoAgendadaService;
import br.ufma.monitoria.backend.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvaliacaoController.class)
class AvaliacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvaliacaoService avaliacaoService;
    @MockBean
    private SessaoAgendadaService sessaoAgendadaService;
    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarAvaliacoesPorSessao() throws Exception {
        Usuario aluno = Usuario.builder().usuarioId(1L).nome("Aluno").build();
        SessaoAgendada sessao = SessaoAgendada.builder().sessaoId(1L).build();
        Avaliacao avaliacao = Avaliacao.builder()
                .avaliacaoId(1L)
                .estrelas(5)
                .comentario("Ótima sessão")
                .sessaoAgendada(sessao)
                .aluno(aluno)
                .build();

        when(avaliacaoService.listarPorSessao(1L)).thenReturn(Collections.singletonList(avaliacao));

        mockMvc.perform(get("/api/avaliacoes/sessao/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comentario").value("Ótima sessão"));
    }
}
