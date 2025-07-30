package br.ufma.monitoria.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.ufma.monitoria.dto.SessaoMonitoriaDTO;
import br.ufma.monitoria.dto.SessaoMonitoriaMapper;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.StatusSessao;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.MonitoriaRepository;
import br.ufma.monitoria.repository.SessaoMonitoriaRepository;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.security.JwtService;
import br.ufma.monitoria.security.TestSecurityConfig;

@WebMvcTest(controllers = SessaoMonitoriaController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = "jwt.secret=chave-falsa-para-testes")

class SessaoMonitoriaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private SessaoMonitoriaRepository sessaoRepo;

  @MockitoBean
  private UsuarioRepository usuarioRepo;

  @MockitoBean
  private MonitoriaRepository monitoriaRepo;

  @MockitoBean
  private SessaoMonitoriaMapper mapper;
  
  @MockitoBean
  private JwtService jwtService;

  private SessaoMonitoria sessao;
  private SessaoMonitoriaDTO dto;
  private Usuario aluno;
  private Monitoria monitoria;

  @BeforeEach
  @SuppressWarnings("unused")
  void setup() {
    aluno = Usuario.builder()
        .id(UUID.randomUUID())
        .nome("Aluno Exemplo")
        .tipo(TipoUsuario.ALUNO)
        .build();

    monitoria = Monitoria.builder()
        .id(UUID.randomUUID())
        .disciplina("Algoritmos")
        .curso("Computação")
        .codigo("MON002")
        .monitor(aluno)
        .link("https://link-sessao")
        .observacoes("Precisa trazer lista")
        .build();

    sessao = SessaoMonitoria.builder()
        .id(UUID.randomUUID())
        .monitoria(monitoria)
        .aluno(aluno)
        .dataHora(LocalDateTime.now().plusDays(1))
        .tema("Revisão de Listas")
        .status(StatusSessao.AGENDADA)
        .build();

    dto = SessaoMonitoriaDTO.builder()
        .id(sessao.getId())
        .monitoriaId(monitoria.getId())
        .alunoId(aluno.getId())
        .dataHora(sessao.getDataHora())
        .tema(sessao.getTema())
        .status(sessao.getStatus())
        .build();
  }

  @Test
  @WithMockUser(roles = "ALUNO")
  void deveListarTodasAsSessoesComSucesso() throws Exception {
    when(sessaoRepo.findAll()).thenReturn(List.of(sessao));
    when(mapper.toDTO(sessao)).thenReturn(dto);

    mockMvc.perform(get("/api/sessoes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].tema").value("Revisão de Listas"));
  }

  @Test
  @WithMockUser(roles = "ALUNO")
  void deveBuscarSessaoPorIdComSucesso() throws Exception {
    UUID id = sessao.getId();

    when(sessaoRepo.findById(id)).thenReturn(Optional.of(sessao));
    when(mapper.toDTO(sessao)).thenReturn(dto);

    mockMvc.perform(get("/api/sessoes/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tema").value("Revisão de Listas"));
  }

  @Test
  @WithMockUser(roles = "ALUNO")
  void deveCriarSessaoComSucesso() throws Exception {
    when(usuarioRepo.findById(dto.getAlunoId())).thenReturn(Optional.of(aluno));
    when(monitoriaRepo.findById(dto.getMonitoriaId())).thenReturn(Optional.of(monitoria));
    when(mapper.toEntity(dto, monitoria, aluno)).thenReturn(sessao);
    when(sessaoRepo.save(sessao)).thenReturn(sessao);
    when(mapper.toDTO(sessao)).thenReturn(dto);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    mockMvc.perform(post("/api/sessoes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tema").value("Revisão de Listas"));
  }

  @Test
  @WithMockUser(roles = "ALUNO")
  void deveListarSessoesPorMonitoriaComSucesso() throws Exception {
    UUID monitoriaId = monitoria.getId();

    when(sessaoRepo.findByMonitoriaId(monitoriaId)).thenReturn(List.of(sessao));
    when(mapper.toDTO(sessao)).thenReturn(dto);

    mockMvc.perform(get("/api/sessoes/por-monitoria/{monitoriaId}", monitoriaId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].monitoriaId").value(monitoriaId.toString()));
  }

  @Test
  @WithMockUser(roles = "ALUNO")
  void deveListarSessoesPorAlunoComSucesso() throws Exception {
    UUID alunoId = aluno.getId();

    when(sessaoRepo.findByAlunoId(alunoId)).thenReturn(List.of(sessao));
    when(mapper.toDTO(sessao)).thenReturn(dto);

    mockMvc.perform(get("/api/sessoes/por-aluno/{alunoId}", alunoId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].alunoId").value(alunoId.toString()));
  }

  @Test
  @WithMockUser(roles = "ALUNO")
  void deveRemoverSessaoComSucesso() throws Exception {
    UUID id = sessao.getId();

    when(sessaoRepo.findById(id)).thenReturn(Optional.of(sessao));

    mockMvc.perform(delete("/api/sessoes/{id}", id))
        .andExpect(status().isOk());
    verify(sessaoRepo).delete(sessao);
  }
}
