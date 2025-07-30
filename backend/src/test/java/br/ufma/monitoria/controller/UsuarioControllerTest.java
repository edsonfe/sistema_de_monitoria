package br.ufma.monitoria.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doNothing;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.ufma.monitoria.dto.UsuarioCadastroDTO;
import br.ufma.monitoria.dto.UsuarioDTO;
import br.ufma.monitoria.dto.UsuarioDetalhadoDTO;
import br.ufma.monitoria.dto.UsuarioMapper;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.security.JwtService;
import br.ufma.monitoria.security.TestSecurityConfig;
import br.ufma.monitoria.service.contract.UsuarioServiceInterface;

@WebMvcTest(UsuarioController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = "jwt.secret=chave-falsa-para-testes")

class UsuarioControllerTest {

        @MockitoBean
        private UsuarioServiceInterface usuarioService;

        @MockitoBean
        private UsuarioMapper usuarioMapper;

        @MockitoBean
        private JwtService jwtService;

        @Autowired
        private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        @Test
        void deveCriarUsuarioComSucesso() throws Exception {
                UsuarioCadastroDTO dto = UsuarioCadastroDTO.builder()
                                .nome("Edson")
                                .email("edson@teste.com")
                                .cpf("12345678901")
                                .matricula("20230001")
                                .curso("Sistemas")
                                .dataNascimento(LocalDate.of(2000, 4, 15))
                                .telefone("98991234567")
                                .tipo(TipoUsuario.ALUNO)
                                .codigoVerificacao("ABC123")
                                .senha("senhaSegura123")
                                .build();

                Usuario entity = Usuario.builder()
                                .id(UUID.randomUUID())
                                .nome(dto.getNome())
                                .email(dto.getEmail())
                                .cpf(dto.getCpf())
                                .matricula(dto.getMatricula())
                                .curso(dto.getCurso())
                                .dataNascimento(dto.getDataNascimento())
                                .telefone(dto.getTelefone())
                                .tipo(dto.getTipo())
                                .codigoVerificacao(dto.getCodigoVerificacao())
                                .senha(dto.getSenha())
                                .build();

                UsuarioDTO responseDTO = UsuarioDTO.builder()
                                .email(dto.getEmail())
                                .cpf(dto.getCpf())
                                .matricula(dto.getMatricula())
                                .tipo(dto.getTipo())
                                .telefone(dto.getTelefone())
                                .build();

                when(usuarioMapper.toEntity(dto)).thenReturn(entity);
                when(usuarioService.criarUsuario(entity)).thenReturn(entity);
                when(usuarioMapper.toDTO(entity)).thenReturn(responseDTO);

                mockMvc.perform(post("/api/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                                .andExpect(jsonPath("$.telefone").value(dto.getTelefone()));
        }
 
        @Test
        @WithMockUser(username = "edson", roles = "ALUNO")
        void deveBuscarUsuarioPorIdComSucesso() throws Exception {
                UUID id = UUID.randomUUID();

                Usuario entity = Usuario.builder()
                                .id(id)
                                .email("edson@teste.com")
                                .cpf("12345678901")
                                .matricula("20230001")
                                .tipo(TipoUsuario.ALUNO)
                                .nome("Edson")
                                .curso("Sistemas")
                                .codigoVerificacao("ABC123")
                                .dataNascimento(LocalDate.of(2000, 4, 15))
                                .telefone("98991234567")
                                .build();

                UsuarioDetalhadoDTO dto = UsuarioDetalhadoDTO.builder()
                                .id(id)
                                .email(entity.getEmail())
                                .cpf(entity.getCpf())
                                .matricula(entity.getMatricula())
                                .tipo(entity.getTipo())
                                .nome(entity.getNome())
                                .curso(entity.getCurso())
                                .codigoVerificacao(entity.getCodigoVerificacao())
                                .dataNascimento(entity.getDataNascimento())
                                .telefone(entity.getTelefone())
                                .build();

                when(usuarioService.buscarPorId(id)).thenReturn(Optional.of(entity));
                when(usuarioMapper.toDetalhadoDTO(entity)).thenReturn(dto);

                mockMvc.perform(get("/api/usuarios/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nome").value("Edson"))
                                .andExpect(jsonPath("$.telefone").value("98991234567"))
                                .andExpect(jsonPath("$.dataNascimento").value("2000-04-15"));
        }

        @Test
        @WithMockUser(username = "edson", roles = "ALUNO")
        void deveRetornar404AoBuscarIdInexistente() throws Exception {
                UUID id = UUID.randomUUID();
                when(usuarioService.buscarPorId(id)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/usuarios/{id}", id))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(username = "edson", roles = "ALUNO")
        void deveBuscarUsuarioPorEmail() throws Exception {
                String email = "edson@teste.com";
                Usuario entity = Usuario.builder()
                                .email(email)
                                .cpf("12345678901")
                                .matricula("20230001")
                                .tipo(TipoUsuario.ALUNO)
                                .telefone("98991234567")
                                .build();

                var dto = UsuarioDTO.builder()
                                .email(email)
                                .cpf("12345678901")
                                .matricula("20230001")
                                .tipo(TipoUsuario.ALUNO)
                                .telefone("98991234567")
                                .build();

                when(usuarioService.buscarPorEmail(email)).thenReturn(Optional.of(entity));
                when(usuarioMapper.toDTO(entity)).thenReturn(dto);

                mockMvc.perform(get("/api/usuarios/email/{email}", email))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.email").value(email))
                                .andExpect(jsonPath("$.telefone").value("98991234567"));
        }

        @Test
        @WithMockUser(username = "edson", roles = "ALUNO")
        void deveBuscarUsuariosPorTipo() throws Exception {
                Usuario entity = Usuario.builder()
                                .email("edson@teste.com")
                                .cpf("12345678901")
                                .matricula("20230001")
                                .tipo(TipoUsuario.ALUNO)
                                .telefone("98991234567")
                                .build();

                UsuarioDTO dto = UsuarioDTO.builder()
                                .email("edson@teste.com")
                                .cpf("12345678901")
                                .matricula("20230001")
                                .tipo(TipoUsuario.ALUNO)
                                .telefone("98991234567")
                                .build();

                when(usuarioService.listarPorTipo(TipoUsuario.ALUNO)).thenReturn(List.of(entity));
                when(usuarioMapper.toDTO(entity)).thenReturn(dto);

                mockMvc.perform(get("/api/usuarios/tipo")
                                .param("tipo", "ALUNO"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].tipo").value("ALUNO"))
                                .andExpect(jsonPath("$[0].telefone").value("98991234567"));
        }

        @Test
        @WithMockUser(username = "edson", roles = "ALUNO")
        void deveAtualizarUsuarioComSucesso() throws Exception {
                UUID id = UUID.randomUUID();

                UsuarioCadastroDTO dtoAtualizado = UsuarioCadastroDTO.builder()
                                .nome("Edson Atualizado")
                                .email("edson@update.com")
                                .cpf("12345678901")
                                .matricula("20230001")
                                .curso("Engenharia")
                                .dataNascimento(LocalDate.of(1998, 5, 20))
                                .telefone("98991112222") // telefone sem espaço, conforme validação
                                .tipo(TipoUsuario.ALUNO)
                                .codigoVerificacao("VER456")
                                .senha("novaSenha456")
                                .build();

                Usuario entityAtualizado = Usuario.builder()
                                .id(id)
                                .nome(dtoAtualizado.getNome())
                                .email(dtoAtualizado.getEmail())
                                .cpf(dtoAtualizado.getCpf())
                                .matricula(dtoAtualizado.getMatricula())
                                .curso(dtoAtualizado.getCurso())
                                .dataNascimento(dtoAtualizado.getDataNascimento())
                                .telefone(dtoAtualizado.getTelefone())
                                .tipo(dtoAtualizado.getTipo())
                                .codigoVerificacao(dtoAtualizado.getCodigoVerificacao())
                                .senha(dtoAtualizado.getSenha())
                                .build();

                UsuarioDTO dtoRetorno = UsuarioDTO.builder()
                                .email(dtoAtualizado.getEmail())
                                .cpf(dtoAtualizado.getCpf())
                                .matricula(dtoAtualizado.getMatricula())
                                .tipo(dtoAtualizado.getTipo())
                                .telefone(dtoAtualizado.getTelefone())
                                .build();

                // mock extra necessário após incluir verificação no controller
                when(usuarioService.buscarPorId(id)).thenReturn(Optional.of(entityAtualizado));

                when(usuarioMapper.toEntity(dtoAtualizado)).thenReturn(entityAtualizado);
                when(usuarioService.salvar(entityAtualizado)).thenReturn(entityAtualizado);
                when(usuarioMapper.toDTO(entityAtualizado)).thenReturn(dtoRetorno);

                mockMvc.perform(put("/api/usuarios/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoAtualizado)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.email").value(dtoAtualizado.getEmail()))
                                .andExpect(jsonPath("$.telefone").value(dtoAtualizado.getTelefone()));
        }

        @Test
        @WithMockUser(username = "edson", roles = "ADMIN")
        void deveExcluirUsuarioComSucesso() throws Exception {
                UUID id = UUID.randomUUID();

                // Simulando que o usuário existe
                when(usuarioService.buscarPorId(id)).thenReturn(Optional.of(new Usuario()));

                // Simulando exclusão sem efeitos colaterais
                doNothing().when(usuarioService).excluirPorId(id);

                mockMvc.perform(delete("/api/usuarios/{id}", id))
                                .andExpect(status().isNoContent());
        }
                                

}
