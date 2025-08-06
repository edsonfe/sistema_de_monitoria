package br.ufma.monitoria.backend.controller;

import br.ufma.monitoria.backend.model.Notificacao;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.service.NotificacaoService;
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

@WebMvcTest(NotificacaoController.class)
class NotificacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacaoService notificacaoService;
    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarNotificacoesPorUsuario() throws Exception {
        Usuario usuario = Usuario.builder().usuarioId(1L).nome("Aluno").build();
        Notificacao notificacao = Notificacao.builder()
                .notificacaoId(1L)
                .mensagem("Nova mensagem")
                .dataCriacao(LocalDateTime.now())
                .usuarioDestino(usuario)
                .lida(false)
                .build();

        when(notificacaoService.listarPorUsuario(1L)).thenReturn(Collections.singletonList(notificacao));

        mockMvc.perform(get("/api/notificacoes/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mensagem").value("Nova mensagem"));
    }
}
