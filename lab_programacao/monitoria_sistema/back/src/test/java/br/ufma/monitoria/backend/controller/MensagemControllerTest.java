package br.ufma.monitoria.backend.controller;

import br.ufma.monitoria.backend.model.Mensagem;
import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.service.MensagemService;
import br.ufma.monitoria.backend.service.SessaoAgendadaService;
import br.ufma.monitoria.backend.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MensagemController.class)
class MensagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MensagemService mensagemService;
    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private SessaoAgendadaService sessaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarMensagensPorSessao() throws Exception {
        Usuario autor = Usuario.builder().usuarioId(1L).nome("Aluno").build();
        SessaoAgendada sessao = SessaoAgendada.builder().sessaoId(1L).build();
        Mensagem mensagem = Mensagem.builder()
                .mensagemId(1L)
                .conteudo("Olá!")
                .autor(autor)
                .sessaoAgendada(sessao)
                .dataHora(LocalDateTime.now())
                .build();

        when(mensagemService.buscarMensagensPorSessao(1L)).thenReturn(Collections.singletonList(mensagem));

        mockMvc.perform(get("/api/mensagens/sessao/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].conteudo").value("Olá!"));
    }
}
