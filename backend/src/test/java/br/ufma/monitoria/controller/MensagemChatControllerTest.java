package br.ufma.monitoria.controller;

import java.time.LocalDateTime;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.ufma.monitoria.dto.MensagemChatDTO;
import br.ufma.monitoria.dto.MensagemChatMapper;
import br.ufma.monitoria.model.MensagemChat;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.StatusSessao;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.SessaoMonitoriaRepository;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.security.JwtService;
import br.ufma.monitoria.security.TestSecurityConfig;
import br.ufma.monitoria.service.contract.MensagemChatServiceInterface;

@TestPropertySource(properties = "jwt.secret=chave-falsa-para-testes")
@WebMvcTest(MensagemChatController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class MensagemChatControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private MensagemChatServiceInterface mensagemService;

        @MockitoBean
        private SessaoMonitoriaRepository sessaoRepo;

        @MockitoBean
        private UsuarioRepository usuarioRepo;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private MensagemChatMapper mapper;

        private Usuario autor;
        private SessaoMonitoria sessao;
        private MensagemChat mensagem;
        private MensagemChatDTO dto;

        @BeforeEach
        @SuppressWarnings("unused")
        void setup() {
                autor = Usuario.builder()
                                .id(UUID.randomUUID())
                                .nome("João")
                                .tipo(TipoUsuario.ALUNO)
                                .build();

                sessao = SessaoMonitoria.builder()
                                .id(UUID.randomUUID())
                                .monitoria(Monitoria.builder().id(UUID.randomUUID()).build())
                                .aluno(autor)
                                .dataHora(LocalDateTime.now().plusHours(1))
                                .tema("Revisão de matéria")
                                .status(StatusSessao.AGENDADA)
                                .build();

                mensagem = MensagemChat.builder()
                                .id(UUID.randomUUID())
                                .conteudo("Olá, tudo certo pra sessão de hoje?")
                                .dataHora(LocalDateTime.now())
                                .autor(autor)
                                .sessaoMonitoria(sessao)
                                .build();

                dto = MensagemChatDTO.builder()
                                .id(mensagem.getId())
                                .conteudo(mensagem.getConteudo())
                                .dataHora(mensagem.getDataHora())
                                .autorId(autor.getId())
                                .sessaoMonitoriaId(sessao.getId())
                                .build();
        }

        @Test
        void deveCriarMensagemComSucesso() throws Exception {
                when(usuarioRepo.findById(dto.getAutorId())).thenReturn(Optional.of(autor));
                when(sessaoRepo.findById(dto.getSessaoMonitoriaId())).thenReturn(Optional.of(sessao));
                when(mapper.toEntity(dto, autor, sessao)).thenReturn(mensagem);
                when(mensagemService.salvarMensagem(mensagem)).thenReturn(mensagem);
                when(mapper.toDTO(mensagem)).thenReturn(dto);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                mockMvc.perform(post("/api/mensagens")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.conteudo").value("Olá, tudo certo pra sessão de hoje?"))
                                .andExpect(jsonPath("$.autorId").value(autor.getId().toString()))
                                .andExpect(jsonPath("$.sessaoMonitoriaId").value(sessao.getId().toString()));
        }

        @Test
        void deveListarMensagensPorSessaoComSucesso() throws Exception {
                UUID sessaoId = sessao.getId();

                when(mensagemService.listarMensagensPorSessao(sessaoId)).thenReturn(List.of(mensagem));
                when(mapper.toDTO(mensagem)).thenReturn(dto);

                mockMvc.perform(get("/api/mensagens/sessao/{sessaoId}", sessaoId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].conteudo").value(dto.getConteudo()))
                                .andExpect(jsonPath("$[0].autorId").value(autor.getId().toString()))
                                .andExpect(jsonPath("$[0].sessaoMonitoriaId").value(sessao.getId().toString()));
        }
}
