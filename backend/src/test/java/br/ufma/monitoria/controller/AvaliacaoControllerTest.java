package br.ufma.monitoria.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.ufma.monitoria.dto.AvaliacaoDTO;
import br.ufma.monitoria.dto.AvaliacaoMapper;
import br.ufma.monitoria.model.Avaliacao;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.StatusSessao;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.SessaoMonitoriaRepository;
import br.ufma.monitoria.security.JwtService;
import br.ufma.monitoria.security.TestSecurityConfig;
import br.ufma.monitoria.service.contract.AvaliacaoServiceInterface;

@WebMvcTest(AvaliacaoController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = "jwt.secret=chave-falsa-para-testes")

class AvaliacaoControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private AvaliacaoServiceInterface avaliacaoService;

        @MockitoBean
        private SessaoMonitoriaRepository sessaoRepo;

        @MockitoBean
        private AvaliacaoMapper mapper;

        @MockitoBean
        private JwtService jwtService;

        private Avaliacao avaliacao;
        private AvaliacaoDTO dto;
        private SessaoMonitoria sessao;

        @BeforeEach
        @SuppressWarnings("unused")
        void setup() {
                sessao = SessaoMonitoria.builder()
                                .id(UUID.randomUUID())
                                .monitoria(Monitoria.builder().id(UUID.randomUUID()).build())
                                .aluno(Usuario.builder().id(UUID.randomUUID()).nome("Maria").build())
                                .dataHora(LocalDateTime.now().plusDays(1))
                                .tema("Cálculo Diferencial")
                                .status(StatusSessao.CONCLUIDA)
                                .build();

                avaliacao = Avaliacao.builder()
                                .id(UUID.randomUUID())
                                .sessaoMonitoria(sessao)
                                .nota(4)
                                .comentario("Muito bom")
                                .data(LocalDateTime.now())
                                .build();

                dto = AvaliacaoDTO.builder()
                                .id(avaliacao.getId())
                                .sessaoMonitoriaId(sessao.getId())
                                .nota(avaliacao.getNota())
                                .comentario(avaliacao.getComentario())
                                .data(avaliacao.getData())
                                .build();
        }

        @Test
        void deveCriarAvaliacaoComSucesso() throws Exception {
                when(sessaoRepo.findById(sessao.getId())).thenReturn(Optional.of(sessao));
                when(mapper.toEntity(any(AvaliacaoDTO.class), eq(sessao))).thenReturn(avaliacao);
                when(avaliacaoService.salvarAvaliacao(avaliacao)).thenReturn(avaliacao);
                when(mapper.toDTO(avaliacao)).thenReturn(dto);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                dto.setData(null); // simula client sem data manual

                mockMvc.perform(post("/api/avaliacoes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nota").value(4))
                                .andExpect(jsonPath("$.comentario").value("Muito bom"))
                                .andExpect(jsonPath("$.sessaoMonitoriaId").value(sessao.getId().toString()));
        }

        @Test
        void deveRetornarAvaliacaoPorSessao() throws Exception {
                when(avaliacaoService.buscarPorSessaoMonitoriaId(sessao.getId())).thenReturn(Optional.of(avaliacao));
                when(mapper.toDTO(avaliacao)).thenReturn(dto);

                mockMvc.perform(get("/api/avaliacoes/sessao/{sessaoId}", sessao.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nota").value(4));
        }

        @Test
        void deveRetornar404SeAvaliacaoNaoEncontrada() throws Exception {
                UUID sessaoId = UUID.randomUUID();
                when(avaliacaoService.buscarPorSessaoMonitoriaId(sessaoId)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/avaliacoes/sessao/{sessaoId}", sessaoId))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deveCalcularMediaComSucesso() throws Exception {
                UUID monitoriaId = sessao.getMonitoria().getId();
                when(avaliacaoService.calcularMediaPorMonitoria(monitoriaId)).thenReturn(4.5);

                mockMvc.perform(get("/api/avaliacoes/monitoria/{monitoriaId}/media", monitoriaId))
                                .andExpect(status().isOk())
                                .andExpect(content().string("4.5"));
        }

        @Test
        void deveRetornarNoContentSeSemAvaliacoes() throws Exception {
                UUID monitoriaId = sessao.getMonitoria().getId();
                when(avaliacaoService.calcularMediaPorMonitoria(monitoriaId)).thenReturn(null);

                mockMvc.perform(get("/api/avaliacoes/monitoria/{monitoriaId}/media", monitoriaId))
                                .andExpect(status().isNoContent());
        }
}
