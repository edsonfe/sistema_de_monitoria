package br.ufma.monitoria.backend.service;

import br.ufma.monitoria.backend.model.Material;
import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialService materialService;

    private Material material;
    private SessaoAgendada sessao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sessao = SessaoAgendada.builder()
                .sessaoId(1L)
                .data(LocalDateTime.now().plusDays(1))
                .build();

        material = Material.builder()
                .materialId(1L)
                .titulo("Lista de Exercícios")
                .linkExterno("http://drive.com/exercicio")
                .dataEnvio(LocalDateTime.now())
                .sessaoAgendada(sessao)
                .build();
    }

    @Test
    void deveEnviarMaterialComSucesso() {
        when(materialRepository.save(material)).thenReturn(material);

        Material salvo = materialService.enviarMaterial(material);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getTitulo()).isEqualTo("Lista de Exercícios");
    }

    @Test
    void deveBuscarMaterialPorId() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        Material encontrado = materialService.buscarPorId(1L);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getLinkExterno()).contains("drive");
    }

    @Test
    void deveListarMateriaisPorSessao() {
        when(materialRepository.findBySessaoAgendada_SessaoId(1L)).thenReturn(List.of(material));

        List<Material> lista = materialService.listarPorSessao(1L);

        assertThat(lista).isNotEmpty();
        assertThat(lista.get(0).getTitulo()).isEqualTo("Lista de Exercícios");
    }

    @Test
    void deveListarMateriaisPorSessaoAposData() {
        LocalDateTime ontem = LocalDateTime.now().minusDays(1);
        when(materialRepository.findBySessaoAgendada_SessaoIdAndDataEnvioAfter(1L, ontem))
                .thenReturn(List.of(material));

        List<Material> lista = materialService.listarPorSessaoAposData(1L, ontem);

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getDataEnvio()).isAfter(ontem);
    }

    @Test
    void deveExcluirMaterialComSucesso() {
        when(materialRepository.existsById(1L)).thenReturn(true);

        materialService.excluirMaterial(1L);

        verify(materialRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveFalharExclusaoMaterialInexistente() {
        when(materialRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> materialService.excluirMaterial(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Material não encontrado para exclusão");
    }
}
