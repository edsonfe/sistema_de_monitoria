package br.ufma.monitoria.backend.controller;

import br.ufma.monitoria.backend.model.*;
import br.ufma.monitoria.backend.service.MonitoriaService;
import br.ufma.monitoria.backend.service.SessaoAgendadaService;
import br.ufma.monitoria.backend.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessaoAgendadaController.class)
class SessaoAgendadaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessaoAgendadaService sessaoService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private MonitoriaService monitoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario aluno;
    private Monitoria monitoria;
    private SessaoAgendada sessao;

    @BeforeEach
    void setUp() {
        aluno = Usuario.builder()
                .usuarioId(1L)
                .nome("Aluno Teste")
                .email("aluno@teste.com")
                .matricula("2021001")
                .tipoUsuario(TipoUsuario.ALUNO)
                .build();

        monitoria = Monitoria.builder()
                .monitoriaId(1L)
                .disciplina("Estrutura de Dados")
                .codigoDisciplina("ED001")
                .monitor(aluno) // só para simplificar
                .build();

        sessao = SessaoAgendada.builder()
                .sessaoId(1L)
                .data(LocalDateTime.now().plusDays(1))
                .status(StatusSessao.AGUARDANDO_APROVACAO)
                .aluno(aluno)
                .monitoria(monitoria)
                .build();
    }

    @Test
    void deveListarSessoesPorAluno() throws Exception {
        Mockito.when(sessaoService.listarPorAluno(1L))
                .thenReturn(Collections.singletonList(sessao));

        mockMvc.perform(get("/api/sessoes/aluno/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sessaoId").value(1L))
                .andExpect(jsonPath("$[0].alunoId").value(1L))
                .andExpect(jsonPath("$[0].disciplinaMonitoria").value("Estrutura de Dados"));
    }

    @Test
    void deveBuscarSessaoPorId() throws Exception {
        Mockito.when(sessaoService.buscarPorId(1L)).thenReturn(sessao);

        mockMvc.perform(get("/api/sessoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessaoId").value(1L));
    }

    @Test
    void deveRetornar404QuandoSessaoNaoEncontrada() throws Exception {
        Mockito.when(sessaoService.buscarPorId(99L))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Sessão não encontrada"));

        mockMvc.perform(get("/api/sessoes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAgendarSessaoComSucesso() throws Exception {
        Mockito.when(usuarioService.buscarPorId(1L)).thenReturn(aluno);
        Mockito.when(monitoriaService.buscarPorId(1L)).thenReturn(monitoria);
        Mockito.when(sessaoService.agendarSessao(any(SessaoAgendada.class))).thenReturn(sessao);

        String json = """
            {
                "alunoId": 1,
                "monitoriaId": 1,
                "data": "%s",
                "status": "AGUARDANDO_APROVACAO"
            }
        """.formatted(sessao.getData());

        mockMvc.perform(post("/api/sessoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessaoId").value(1L))
                .andExpect(jsonPath("$.status").value("AGUARDANDO_APROVACAO"));
    }

    @Test
    void deveAtualizarStatusDaSessao() throws Exception {
        SessaoAgendada atualizada = sessao.toBuilder()
                .status(StatusSessao.DEFERIDA)
                .build();

        Mockito.when(sessaoService.atualizarStatus(1L, StatusSessao.DEFERIDA))
                .thenReturn(atualizada);

        mockMvc.perform(patch("/api/sessoes/1/status?status=DEFERIDA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DEFERIDA"));
    }

    @Test
    void deveCancelarSessao() throws Exception {
        mockMvc.perform(delete("/api/sessoes/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(sessaoService).cancelarSessao(1L);
    }
}
