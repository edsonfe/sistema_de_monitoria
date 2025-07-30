package br.ufma.monitoria.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufma.monitoria.dto.MonitoriaCadastroDTO;
import br.ufma.monitoria.dto.MonitoriaMapper;
import br.ufma.monitoria.dto.MonitoriaRespostaDTO;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.security.JwtService;
import br.ufma.monitoria.security.TestSecurityConfig;
import br.ufma.monitoria.service.impl.MonitoriaService;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@WebMvcTest(MonitoriaController.class)
@TestPropertySource(properties = "jwt.secret=chave-falsa-para-testes")
class MonitoriaControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private MonitoriaService monitoriaService;

        @MockitoBean
        private MonitoriaMapper monitoriaMapper;

        @MockitoBean
        private JwtService jwtService;

        @Autowired
        private ObjectMapper objectMapper;

        private Usuario monitor;
        private Monitoria baseMonitoria;

        @BeforeEach
        @SuppressWarnings("unused")
        void setup() {
                monitor = Usuario.builder()
                                .id(UUID.randomUUID())
                                .nome("Monitor Edson")
                                .tipo(TipoUsuario.MONITOR)
                                .build();

                baseMonitoria = Monitoria.builder()
                                .id(UUID.randomUUID())
                                .disciplina("Estrutura de Dados")
                                .curso("Computação")
                                .codigo("MON001")
                                .link("https://link-monitoria")
                                .observacoes("Sem observações")
                                .monitor(monitor)
                                .build();
        }

        private MonitoriaRespostaDTO criarDTO(Monitoria m) {
                return MonitoriaRespostaDTO.builder()
                                .id(m.getId())
                                .disciplina(m.getDisciplina())
                                .curso(m.getCurso())
                                .codigo(m.getCodigo())
                                .link(m.getLink())
                                .observacoes(m.getObservacoes())
                                .monitorId(m.getMonitor().getId())
                                .build();
        }

        @Test
        void deveCriarMonitoriaComSucesso() throws Exception {
                MonitoriaCadastroDTO cadastroDTO = MonitoriaCadastroDTO.builder()
                                .curso(baseMonitoria.getCurso())
                                .disciplina(baseMonitoria.getDisciplina())
                                .codigo(baseMonitoria.getCodigo())
                                .link(baseMonitoria.getLink())
                                .observacoes(baseMonitoria.getObservacoes())
                                .monitorId(monitor.getId())
                                .build();

                when(monitoriaService.criar(cadastroDTO)).thenReturn(baseMonitoria);
                when(monitoriaMapper.toDTO(baseMonitoria)).thenReturn(criarDTO(baseMonitoria));

                mockMvc.perform(post("/api/monitorias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(cadastroDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.disciplina").value("Estrutura de Dados"));
        }

        @Test
        void deveBuscarPorIdComSucesso() throws Exception {
                UUID id = baseMonitoria.getId();
                when(monitoriaService.buscarPorId(id)).thenReturn(Optional.of(baseMonitoria));
                when(monitoriaMapper.toDTO(baseMonitoria)).thenReturn(criarDTO(baseMonitoria));

                mockMvc.perform(get("/api/monitorias/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.disciplina").value(baseMonitoria.getDisciplina()));
        }

        @Test
        void deveListarPorCursoComSucesso() throws Exception {
                String curso = baseMonitoria.getCurso();

                when(monitoriaService.listarPorCurso(curso)).thenReturn(List.of(baseMonitoria));
                when(monitoriaMapper.toDTO(baseMonitoria)).thenReturn(criarDTO(baseMonitoria));

                mockMvc.perform(get("/api/monitorias/por-curso")
                                .param("curso", curso))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].curso").value(curso));
        }

        @Test
        void deveListarPorDisciplinaComSucesso() throws Exception {
                String disciplina = baseMonitoria.getDisciplina();

                when(monitoriaService.listarPorDisciplina(disciplina)).thenReturn(List.of(baseMonitoria));
                when(monitoriaMapper.toDTO(baseMonitoria)).thenReturn(criarDTO(baseMonitoria));

                mockMvc.perform(get("/api/monitorias/por-disciplina")
                                .param("disciplina", disciplina))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].disciplina").value(disciplina));
        }

        @Test
        void deveListarPorMonitorComSucesso() throws Exception {
                UUID monitorId = monitor.getId();

                when(monitoriaService.listarPorMonitor(monitorId)).thenReturn(List.of(baseMonitoria));
                when(monitoriaMapper.toDTO(baseMonitoria)).thenReturn(criarDTO(baseMonitoria));

                mockMvc.perform(get("/api/monitorias/por-monitor/{monitorId}", monitorId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].monitorId").value(monitorId.toString()));
        }

        @Test
        void deveListarTodasAsMonitorias() throws Exception {
                when(monitoriaService.listarTodos()).thenReturn(List.of(baseMonitoria));
                when(monitoriaMapper.toDTO(baseMonitoria)).thenReturn(criarDTO(baseMonitoria));

                mockMvc.perform(get("/api/monitorias"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].disciplina").value("Estrutura de Dados"));
        }

        @Test
        void deveRemoverMonitoriaComSucesso() throws Exception {
                UUID id = UUID.randomUUID();

                mockMvc.perform(delete("/api/monitorias/{id}", id))
                                .andExpect(status().isOk());
        }

}
