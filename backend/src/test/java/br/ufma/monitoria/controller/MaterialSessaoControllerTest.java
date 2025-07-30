package br.ufma.monitoria.controller;

import java.time.LocalDate;
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

import br.ufma.monitoria.dto.MaterialSessaoDTO;
import br.ufma.monitoria.dto.MaterialSessaoMapper;
import br.ufma.monitoria.model.MaterialSessao;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.StatusSessao;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.SessaoMonitoriaRepository;
import br.ufma.monitoria.security.JwtService;
import br.ufma.monitoria.security.TestSecurityConfig;
import br.ufma.monitoria.service.contract.MaterialSessaoServiceInterface;

@WebMvcTest(MaterialSessaoController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = "jwt.secret=chave-falsa-para-testes")

class MaterialSessaoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MaterialSessaoServiceInterface materialService;

  @MockitoBean
  private SessaoMonitoriaRepository sessaoRepo;

  @MockitoBean
  private MaterialSessaoMapper mapper;

  @MockitoBean
  private JwtService jwtService;

  private MaterialSessao material;
  private MaterialSessaoDTO dto;
  private SessaoMonitoria sessao;
  private ObjectMapper objectMapper;

  @BeforeEach
  @SuppressWarnings("unused")
  void setup() {
    Usuario aluno = Usuario.builder().id(UUID.randomUUID()).nome("André").build();
    Monitoria monitoria = Monitoria.builder().id(UUID.randomUUID()).disciplina("BD").build();

    sessao = SessaoMonitoria.builder()
        .id(UUID.randomUUID())
        .monitoria(monitoria)
        .aluno(aluno)
        .tema("SQL Básico")
        .dataHora(LocalDate.now().atStartOfDay().plusHours(10))
        .status(StatusSessao.CONCLUIDA)
        .build();

    material = MaterialSessao.builder()
        .id(UUID.randomUUID())
        .titulo("Slides SQL")
        .link("https://material.com/sql.pdf")
        .arquivo("/caminho/arquivo/sql.pdf")
        .sessaoMonitoria(sessao)
        .dataEnvio(LocalDate.now())
        .build();

    dto = MaterialSessaoDTO.builder()
        .id(material.getId())
        .titulo(material.getTitulo())
        .link(material.getLink())
        .arquivo(material.getArquivo())
        .sessaoMonitoriaId(sessao.getId())
        .dataEnvio(material.getDataEnvio())
        .build();

    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  void deveCriarMaterialComSucesso() throws Exception {
    when(sessaoRepo.findById(dto.getSessaoMonitoriaId())).thenReturn(Optional.of(sessao));
    when(mapper.toEntity(dto, sessao)).thenReturn(material);
    when(materialService.salvar(material)).thenReturn(material);
    when(mapper.toDTO(material)).thenReturn(dto);

    mockMvc.perform(post("/api/material-sessao")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.titulo").value("Slides SQL"))
        .andExpect(jsonPath("$.sessaoMonitoriaId").value(sessao.getId().toString()));
  }

  @Test
  void deveBuscarMaterialPorIdComSucesso() throws Exception {
    when(materialService.buscarPorId(material.getId())).thenReturn(material);
    when(mapper.toDTO(material)).thenReturn(dto);

    mockMvc.perform(get("/api/material-sessao/{id}", material.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.titulo").value("Slides SQL"));
  }

  @Test
  void deveListarMateriaisPorSessaoComSucesso() throws Exception {
    UUID sessaoId = sessao.getId();

    when(materialService.buscarPorSessao(sessaoId)).thenReturn(List.of(material));
    when(mapper.toDTO(material)).thenReturn(dto);

    mockMvc.perform(get("/api/material-sessao/sessao/{sessaoId}", sessaoId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].sessaoMonitoriaId").value(sessaoId.toString()));
  }

  @Test
  void deveDeletarMaterialComSucesso() throws Exception {
    UUID id = material.getId();

    mockMvc.perform(delete("/api/material-sessao/{id}", id))
        .andExpect(status().isNoContent());

    verify(materialService).deletar(id);
  }
}
