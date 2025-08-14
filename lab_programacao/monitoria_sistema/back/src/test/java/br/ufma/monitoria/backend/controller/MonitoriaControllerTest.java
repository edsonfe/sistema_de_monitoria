package br.ufma.monitoria.backend.controller;

import br.ufma.monitoria.backend.dto.MonitoriaRequestDTO;
import br.ufma.monitoria.backend.model.Curso;
import br.ufma.monitoria.backend.model.Monitoria;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.service.CursoService;
import br.ufma.monitoria.backend.service.MonitoriaService;
import br.ufma.monitoria.backend.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MonitoriaController.class)
public class MonitoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MonitoriaService monitoriaService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private CursoService cursoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarTodasMonitorias() throws Exception {
        Usuario monitor = Usuario.builder().usuarioId(1L).nome("Monitor Teste").build();
        Curso curso = Curso.builder().cursoId(1L).nome("Engenharia").build();

        Monitoria monitoria = Monitoria.builder()
                .monitoriaId(1L)
                .disciplina("Matemática")
                .codigoDisciplina("MAT101")
                .diasDaSemana("Seg, Qua, Sex")
                .horario(LocalTime.of(14, 0))
                .linkSalaVirtual("http://zoom.com")
                .observacoes("Observação teste")
                .monitor(monitor)
                .curso(curso)
                .build();

        when(monitoriaService.buscarPorParteDisciplina("")).thenReturn(Collections.singletonList(monitoria));

        mockMvc.perform(get("/api/monitorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].disciplina").value("Matemática"))
                .andExpect(jsonPath("$[0].monitorNome").value("Monitor Teste"))
                .andExpect(jsonPath("$[0].cursoNome").value("Engenharia"));
    }

    @Test
    void deveCriarMonitoria() throws Exception {
        MonitoriaRequestDTO requestDTO = MonitoriaRequestDTO.builder()
                .disciplina("Matemática")
                .codigoDisciplina("MAT101")
                .diasDaSemana("Seg, Qua, Sex")
                .horario(LocalTime.of(14, 0))
                .linkSalaVirtual("http://zoom.com")
                .observacoes("Observação teste")
                .monitorId(1L)
                .cursoId(1L)
                .build();

        Usuario monitor = Usuario.builder().usuarioId(1L).nome("Monitor Teste").build();
        Curso curso = Curso.builder().cursoId(1L).nome("Engenharia").build();

        Monitoria monitoria = Monitoria.builder()
                .monitoriaId(1L)
                .disciplina(requestDTO.getDisciplina())
                .codigoDisciplina(requestDTO.getCodigoDisciplina())
                .diasDaSemana(requestDTO.getDiasDaSemana())
                .horario(requestDTO.getHorario())
                .linkSalaVirtual(requestDTO.getLinkSalaVirtual())
                .observacoes(requestDTO.getObservacoes())
                .monitor(monitor)
                .curso(curso)
                .build();

        when(usuarioService.buscarPorId(1L)).thenReturn(monitor);
        when(cursoService.buscarPorId(1L)).thenReturn(curso);
        when(monitoriaService.criarMonitoria(any(Monitoria.class))).thenReturn(monitoria);

        mockMvc.perform(post("/api/monitorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disciplina").value("Matemática"))
                .andExpect(jsonPath("$.monitorNome").value("Monitor Teste"))
                .andExpect(jsonPath("$.cursoNome").value("Engenharia"));
    }

    @Test
    void deveRetornarNotFoundAoBuscarMonitoriaInexistente() throws Exception {
        when(monitoriaService.buscarPorId(999L)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/monitorias/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRemoverMonitoria() throws Exception {
        // remover não lança exceção => sucesso
        Mockito.doNothing().when(monitoriaService).removerMonitoria(1L);

        mockMvc.perform(delete("/api/monitorias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void removerMonitoriaInexistenteDeveRetornarNotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException()).when(monitoriaService).removerMonitoria(999L);

        mockMvc.perform(delete("/api/monitorias/999"))
                .andExpect(status().isNotFound());
    }
}
