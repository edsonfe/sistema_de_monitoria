package br.ufma.monitoria.service;

import br.ufma.monitoria.model.MaterialSessao;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.repository.MaterialSessaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.ufma.monitoria.service.impl.MaterialSessaoService;
class MaterialSessaoServiceTest {

    @Mock
    private MaterialSessaoRepository repository;

    @InjectMocks
    private MaterialSessaoService service;

    private MaterialSessao material;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SessaoMonitoria sessao = SessaoMonitoria.builder().id(UUID.randomUUID()).build();

        material = MaterialSessao.builder()
            .id(UUID.randomUUID())
            .titulo("Lista de exercícios")
            .sessaoMonitoria(sessao)
            .dataEnvio(LocalDate.now())
            .build();
    }

    @Test
    void deveSalvarMaterialComDadosValidos() {
        when(repository.save(any(MaterialSessao.class))).thenReturn(material);

        MaterialSessao salvo = service.salvar(material);

        assertNotNull(salvo);
        assertEquals("Lista de exercícios", salvo.getTitulo());
        verify(repository, times(1)).save(material);
    }

    @Test
    void deveLancarErroSeTituloNulo() {
        material.setTitulo(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.salvar(material));
        assertEquals("O título do material não pode estar vazio", ex.getMessage());
    }

    @Test
    void deveLancarErroSeDataEnvioNula() {
        material.setDataEnvio(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.salvar(material));
        assertEquals("A data de envio deve ser informada", ex.getMessage());
    }

    @Test
    void deveBuscarMaterialPorSessao() {
        UUID sessaoId = material.getSessaoMonitoria().getId();
        when(repository.findBySessaoMonitoriaId(sessaoId)).thenReturn(List.of(material));

        List<MaterialSessao> materiais = service.buscarPorSessao(sessaoId);

        assertEquals(1, materiais.size());
        verify(repository).findBySessaoMonitoriaId(sessaoId);
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        UUID id = material.getId();
        when(repository.findById(id)).thenReturn(Optional.of(material));

        MaterialSessao encontrado = service.buscarPorId(id);

        assertEquals("Lista de exercícios", encontrado.getTitulo());
        verify(repository).findById(id);
    }

    @Test
    void deveLancarErroSeBuscarPorIdInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(id));
        assertEquals("Material não encontrado", ex.getMessage());
    }

    @Test
    void deveDeletarMaterialPorId() {
        UUID id = material.getId();

        service.deletar(id);

        verify(repository, times(1)).deleteById(id);
    }
}