package br.ufma.monitoria.backend.controller;

import br.ufma.monitoria.backend.dto.MaterialRequestDTO;
import br.ufma.monitoria.backend.model.Material;
import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.service.MaterialService;
import br.ufma.monitoria.backend.service.SessaoAgendadaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MaterialController.class)
class MaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MaterialService materialService;

    @MockBean
    private SessaoAgendadaService sessaoAgendadaService;

    private SessaoAgendada sessao;
    private Material material;

    @BeforeEach
    void setup() {
        sessao = SessaoAgendada.builder()
                .sessaoId(1L)
                .data(LocalDateTime.now().plusDays(1))
                .build();

        material = Material.builder()
                .materialId(1L)
                .titulo("Slide Aula 01")
                .linkExterno("http://exemplo.com/slide.pdf")
                .dataEnvio(LocalDateTime.now())
                .sessaoAgendada(sessao)
                .build();
    }

    @Test
    void deveEnviarMaterial() throws Exception {
        MaterialRequestDTO dto = new MaterialRequestDTO();
        dto.setTitulo("Slide Aula 01");
        dto.setLink("http://exemplo.com/slide.pdf");
        dto.setSessaoId(1L);

        when(sessaoAgendadaService.buscarPorId(1L)).thenReturn(sessao);
        when(materialService.enviarMaterial(any(Material.class))).thenReturn(material);

        mockMvc.perform(post("/api/materiais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.materialId").value(1L))
                .andExpect(jsonPath("$.titulo").value("Slide Aula 01"))
                .andExpect(jsonPath("$.link").value("http://exemplo.com/slide.pdf"))
                .andExpect(jsonPath("$.sessaoId").value(1L));

        verify(materialService, times(1)).enviarMaterial(any(Material.class));
    }

    @Test
    void deveBuscarMaterialPorId() throws Exception {
        when(materialService.buscarPorId(1L)).thenReturn(material);

        mockMvc.perform(get("/api/materiais/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.materialId").value(1L))
                .andExpect(jsonPath("$.titulo").value("Slide Aula 01"));
    }

    @Test
    void deveListarMateriaisPorSessao() throws Exception {
        List<Material> lista = Arrays.asList(material);
        when(materialService.listarPorSessao(1L)).thenReturn(lista);

        mockMvc.perform(get("/api/materiais/sessao/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].materialId").value(1L))
                .andExpect(jsonPath("$[0].titulo").value("Slide Aula 01"));
    }

    @Test
    void deveListarMateriaisPorSessaoAposData() throws Exception {
        List<Material> lista = Arrays.asList(material);
        when(materialService.listarPorSessaoAposData(eq(1L), any(LocalDateTime.class)))
                .thenReturn(lista);

        mockMvc.perform(get("/api/materiais/sessao/1/apos")
                        .param("dataEnvio", LocalDateTime.now().minusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].materialId").value(1L))
                .andExpect(jsonPath("$[0].titulo").value("Slide Aula 01"));
    }

    @Test
    void deveExcluirMaterial() throws Exception {
        doNothing().when(materialService).excluirMaterial(1L);

        mockMvc.perform(delete("/api/materiais/1"))
                .andExpect(status().isNoContent());

        verify(materialService, times(1)).excluirMaterial(1L);
    }
}
