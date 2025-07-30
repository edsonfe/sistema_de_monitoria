package br.ufma.monitoria.controller;

import br.ufma.monitoria.dto.AgendamentoMonitoriaDTO;
import br.ufma.monitoria.dto.AgendamentoMonitoriaMapper;
import br.ufma.monitoria.model.AgendamentoMonitoria;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.security.JwtService;
import br.ufma.monitoria.security.TestSecurityConfig;
import br.ufma.monitoria.service.contract.AgendamentoMonitoriaServiceInterface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

@WebMvcTest(AgendamentoMonitoriaController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = "jwt.secret=chave-falsa-para-testes")
class AgendamentoMonitoriaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AgendamentoMonitoriaServiceInterface agendamentoService;

  @MockitoBean
  private AgendamentoMonitoriaMapper mapper;

  @MockitoBean
  private JwtService jwtService;

  private Monitoria monitoria;
  private AgendamentoMonitoria agendamento;
  private AgendamentoMonitoriaDTO dto;
  private ObjectMapper objectMapper;

  @BeforeEach
  @SuppressWarnings("unused")
  void setup() {
    monitoria = Monitoria.builder()
        .id(UUID.randomUUID())
        .disciplina("Estrutura de Dados")
        .curso("Computação")
        .codigo("MON003")
        .monitor(null)
        .link("https://reuniao-monitoria")
        .observacoes("Traga dúvidas específicas")
        .build();

    agendamento = AgendamentoMonitoria.builder()
        .id(UUID.randomUUID())
        .monitoria(monitoria)
        .dia(LocalDate.now().plusDays(2))
        .horario(LocalTime.of(14, 30))
        .build();

    dto = AgendamentoMonitoriaDTO.builder()
        .id(agendamento.getId())
        .monitoriaId(monitoria.getId())
        .dia(agendamento.getDia())
        .horario(agendamento.getHorario())
        .build();

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    JavaTimeModule timeModule = new JavaTimeModule();
    timeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));

    objectMapper = new ObjectMapper();
    objectMapper.registerModule(timeModule);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Test
  @WithMockUser(roles = "MONITOR")
  void deveCriarAgendamentoComSucesso() throws Exception {
    when(agendamentoService.criarAgendamento(dto.getMonitoriaId(), dto.getDia(), dto.getHorario()))
        .thenReturn(agendamento);
    when(mapper.toDTO(agendamento)).thenReturn(dto);

    mockMvc.perform(post("/api/agendamentos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.monitoriaId").value(dto.getMonitoriaId().toString()))
        .andExpect(jsonPath("$.dia").value(dto.getDia().toString()))
        .andExpect(jsonPath("$.horario").value(dto.getHorario().format(DateTimeFormatter.ofPattern("HH:mm"))));
  }

  @Test
  @WithMockUser
  void deveBuscarAgendamentosPorMonitoria() throws Exception {
    UUID monitoriaId = monitoria.getId();

    when(agendamentoService.buscarPorMonitoria(monitoriaId)).thenReturn(List.of(agendamento));
    when(mapper.toDTO(agendamento)).thenReturn(dto);

    mockMvc.perform(get("/api/agendamentos/monitoria/{monitoriaId}", monitoriaId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].monitoriaId").value(monitoriaId.toString()));
  }

  @Test
  @WithMockUser
  void deveBuscarAgendamentosPorDia() throws Exception {
    LocalDate dia = agendamento.getDia();

    when(agendamentoService.buscarPorDia(dia)).thenReturn(List.of(agendamento));
    when(mapper.toDTO(agendamento)).thenReturn(dto);

    mockMvc.perform(get("/api/agendamentos/dia/{dia}", dia))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].dia").value(dia.toString()));
  }

  @Test
  @WithMockUser
  void deveVerificarSeAgendamentoExiste() throws Exception {
    UUID monitoriaId = monitoria.getId();
    LocalDate dia = agendamento.getDia();
    LocalTime horario = agendamento.getHorario();

    when(agendamentoService.existeAgendamento(monitoriaId, dia, horario)).thenReturn(true);

    mockMvc.perform(get("/api/agendamentos/existe")
        .param("monitoriaId", monitoriaId.toString())
        .param("dia", dia.toString())
        .param("horario", horario.format(DateTimeFormatter.ofPattern("HH:mm"))))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  @WithMockUser(roles = "MONITOR")
  void deveAtualizarAgendamentoComSucesso() throws Exception {
    UUID id = agendamento.getId();

    when(agendamentoService.atualizarAgendamento(id, dto.getMonitoriaId(), dto.getDia(), dto.getHorario()))
        .thenReturn(agendamento);
    when(mapper.toDTO(agendamento)).thenReturn(dto);

    mockMvc.perform(put("/api/agendamentos/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.monitoriaId").value(dto.getMonitoriaId().toString()))
        .andExpect(jsonPath("$.horario").value(dto.getHorario().format(DateTimeFormatter.ofPattern("HH:mm"))));
  }

  @Test
  @WithMockUser(roles = "MONITOR")
  void deveExcluirAgendamentoComSucesso() throws Exception {
    UUID id = agendamento.getId();
    mockMvc.perform(delete("/api/agendamentos/{id}", id))
        .andExpect(status().isNoContent());
  }
}
