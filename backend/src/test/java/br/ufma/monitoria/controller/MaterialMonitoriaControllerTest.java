package br.ufma.monitoria.controller;

import java.time.LocalDate;
import java.util.List;
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

import br.ufma.monitoria.dto.MaterialMonitoriaDTO;
import br.ufma.monitoria.dto.MaterialMonitoriaMapper;
import br.ufma.monitoria.model.MaterialMonitoria;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.security.JwtService;
import br.ufma.monitoria.security.TestSecurityConfig;
import br.ufma.monitoria.service.contract.MaterialMonitoriaServiceInterface;

@WebMvcTest(MaterialMonitoriaController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = "jwt.secret=chave-falsa-para-testes")

class MaterialMonitoriaControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private MaterialMonitoriaServiceInterface service;

        @MockitoBean
        private MaterialMonitoriaMapper mapper;

        @MockitoBean
        private JwtService jwtService;

        private Monitoria monitoria;
        private MaterialMonitoria material;
        private MaterialMonitoriaDTO dto;
        private ObjectMapper objectMapper;

        @BeforeEach
        @SuppressWarnings("unused")
        void setup() {
                monitoria = Monitoria.builder()
                                .id(UUID.randomUUID())
                                .disciplina("Programação")
                                .curso("Computação")
                                .codigo("MON004")
                                .build();

                material = MaterialMonitoria.builder()
                                .id(UUID.randomUUID())
                                .titulo("Lista de Exercícios")
                                .link("https://materiais.com/lista.pdf")
                                .arquivo("/caminho/arquivo.pdf")
                                .monitoria(monitoria)
                                .dataEnvio(LocalDate.now())
                                .build();

                dto = MaterialMonitoriaDTO.builder()
                                .id(material.getId())
                                .titulo(material.getTitulo())
                                .link(material.getLink())
                                .arquivo(material.getArquivo())
                                .monitoriaId(monitoria.getId())
                                .dataEnvio(material.getDataEnvio())
                                .build();

                objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
        }

        @Test
        void deveSalvarMaterialComSucesso() throws Exception {
                when(mapper.toEntity(dto, null)).thenReturn(material);
                when(service.salvar(material)).thenReturn(material);
                when(mapper.toDTO(material)).thenReturn(dto);

                mockMvc.perform(post("/api/materiais-monitoria")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.titulo").value("Lista de Exercícios"));
        }

        @Test
        void deveListarMateriaisPorMonitoria() throws Exception {
                UUID monitoriaId = monitoria.getId();

                when(service.listarPorMonitoria(monitoriaId)).thenReturn(List.of(material));
                when(mapper.toDTO(material)).thenReturn(dto);

                mockMvc.perform(get("/api/materiais-monitoria/monitoria/{monitoriaId}", monitoriaId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].monitoriaId").value(monitoriaId.toString()));
        }

        @Test
        void deveBuscarMaterialPorIdComSucesso() throws Exception {
                UUID id = material.getId();

                when(service.buscarPorId(id)).thenReturn(material);
                when(mapper.toDTO(material)).thenReturn(dto);

                mockMvc.perform(get("/api/materiais-monitoria/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.titulo").value("Lista de Exercícios"));
        }

        @Test
        void deveRetornar404AoBuscarMaterialInexistente() throws Exception {
                UUID idInvalido = UUID.randomUUID();

                when(service.buscarPorId(idInvalido)).thenReturn(null);

                mockMvc.perform(get("/api/materiais-monitoria/{id}", idInvalido))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deveExcluirMaterialComSucesso() throws Exception {
                UUID id = material.getId();

                mockMvc.perform(delete("/api/materiais-monitoria/{id}", id))
                                .andExpect(status().isNoContent());

                verify(service).excluir(id);
        }
}
