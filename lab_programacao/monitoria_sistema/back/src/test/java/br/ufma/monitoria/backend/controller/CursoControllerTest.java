package br.ufma.monitoria.backend.controller;

import br.ufma.monitoria.backend.dto.CursoRequestDTO;
import br.ufma.monitoria.backend.model.Curso;
import br.ufma.monitoria.backend.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CursoController.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CursoService cursoService;

    @Test
    void deveCriarCurso() throws Exception {
        CursoRequestDTO request = CursoRequestDTO.builder()
                .nome("Engenharia de Software")
                .codigo("ESW123")
                .build();

        Curso cursoSalvo = Curso.builder()
                .cursoId(1L)
                .nome("Engenharia de Software")
                .codigo("ESW123")
                .build();

        Mockito.when(cursoService.cadastrarCurso(Mockito.any(Curso.class))).thenReturn(cursoSalvo);

        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cursoId").value(1L))
                .andExpect(jsonPath("$.nome").value("Engenharia de Software"))
                .andExpect(jsonPath("$.codigo").value("ESW123"));
    }

    @Test
    void deveListarCursos() throws Exception {
        List<Curso> cursos = Arrays.asList(
                Curso.builder().cursoId(1L).nome("Engenharia").codigo("ENG01").build(),
                Curso.builder().cursoId(2L).nome("Computação").codigo("COMP01").build()
        );

        Mockito.when(cursoService.listarTodos()).thenReturn(cursos);

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Engenharia"))
                .andExpect(jsonPath("$[1].nome").value("Computação"));
    }

    @Test
    void deveBuscarCursoPorId() throws Exception {
        Curso curso = Curso.builder()
                .cursoId(10L)
                .nome("Matemática")
                .codigo("MAT01")
                .build();

        Mockito.when(cursoService.buscarPorId(10L)).thenReturn(curso);

        mockMvc.perform(get("/api/cursos/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cursoId").value(10L))
                .andExpect(jsonPath("$.nome").value("Matemática"))
                .andExpect(jsonPath("$.codigo").value("MAT01"));
    }

    @Test
    void deveBuscarCursoPorCodigo() throws Exception {
        Curso curso = Curso.builder()
                .cursoId(5L)
                .nome("Física")
                .codigo("FIS01")
                .build();

        Mockito.when(cursoService.buscarPorCodigo("FIS01")).thenReturn(curso);

        mockMvc.perform(get("/api/cursos/codigo/FIS01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cursoId").value(5L))
                .andExpect(jsonPath("$.nome").value("Física"))
                .andExpect(jsonPath("$.codigo").value("FIS01"));
    }

    @Test
    void deveRemoverCurso() throws Exception {
        mockMvc.perform(delete("/api/cursos/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(cursoService).removerCurso(1L);
    }
}
