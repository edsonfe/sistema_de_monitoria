package br.ufma.monitoria.backend.controller;

import br.ufma.monitoria.backend.dto.UsuarioRequestDTO;
import br.ufma.monitoria.backend.model.Curso;
import br.ufma.monitoria.backend.model.TipoUsuario;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.service.CursoService;
import br.ufma.monitoria.backend.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private CursoService cursoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;
    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = Curso.builder()
                .cursoId(1L)
                .nome("Ciência da Computação")
                .codigo("CCOMP")
                .build();

        usuario = Usuario.builder()
                .usuarioId(1L)
                .nome("João")
                .email("joao@example.com")
                .senha("123")
                .matricula("2023001")
                .tipoUsuario(TipoUsuario.ALUNO)
                .curso(curso)
                .build();
    }

    @Test
    void deveListarUsuarios() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario);

        Mockito.when(usuarioService.listarTodos()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(1L))
                .andExpect(jsonPath("$[0].nome").value("João"))
                .andExpect(jsonPath("$[0].cursoNome").value("Ciência da Computação"));
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        Mockito.when(usuarioService.buscarPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(1L))
                .andExpect(jsonPath("$.nome").value("João"));
    }

    @Test
    void deveRetornar404QuandoUsuarioNaoEncontrado() throws Exception {
        Mockito.when(usuarioService.buscarPorId(99L)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveCadastrarUsuario() throws Exception {
        UsuarioRequestDTO dto = UsuarioRequestDTO.builder()
                .nome("Maria")
                .email("maria@example.com")
                .senha("123")
                .matricula("2023002")
                .tipoUsuario(TipoUsuario.ALUNO)
                .cursoId(1L)
                .build();

        Mockito.when(cursoService.buscarPorId(1L)).thenReturn(curso);
        Mockito.when(usuarioService.cadastrarUsuario(any(Usuario.class), any())).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João")); // retorna mock do usuario
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        UsuarioRequestDTO dto = UsuarioRequestDTO.builder()
                .nome("João Atualizado")
                .email("joao@example.com")
                .senha("123")
                .matricula("2023001")
                .tipoUsuario(TipoUsuario.ALUNO)
                .cursoId(1L)
                .build();

        usuario.setNome("João Atualizado");

        Mockito.when(cursoService.buscarPorId(1L)).thenReturn(curso);
        Mockito.when(usuarioService.atualizarUsuario(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Atualizado"));
    }

    @Test
    void deveRemoverUsuario() throws Exception {
        Mockito.doNothing().when(usuarioService).removerUsuario(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveVerificarExistenciaDeMatricula() throws Exception {
        Mockito.when(usuarioService.existsByMatricula("2023001")).thenReturn(true);

        mockMvc.perform(get("/api/usuarios/exists/matricula/2023001"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deveVerificarExistenciaDeEmail() throws Exception {
        Mockito.when(usuarioService.existsByEmail("joao@example.com")).thenReturn(true);

        mockMvc.perform(get("/api/usuarios/exists/email/joao@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
