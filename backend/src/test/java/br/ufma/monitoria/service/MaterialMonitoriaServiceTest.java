package br.ufma.monitoria.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ufma.monitoria.model.MaterialMonitoria;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.repository.MaterialMonitoriaRepository;
import br.ufma.monitoria.service.impl.MaterialMonitoriaService;

class MaterialMonitoriaServiceTest {

    private MaterialMonitoriaRepository repository;
    private MaterialMonitoriaService service;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        repository = mock(MaterialMonitoriaRepository.class);
        service = new MaterialMonitoriaService();
        service.repository = repository;
    }

    @Test
    void testSalvarMaterial() {
        MaterialMonitoria material = gerarMaterial();
        when(repository.save(material)).thenReturn(material);

        MaterialMonitoria resultado = service.salvar(material);

        assertEquals(material, resultado);
        verify(repository).save(material);
    }

    @Test
    void testBuscarPorId() {
        UUID id = UUID.randomUUID();
        MaterialMonitoria material = gerarMaterial();
        when(repository.findById(id)).thenReturn(Optional.of(material));

        MaterialMonitoria resultado = service.buscarPorId(id);

        assertEquals(material, resultado);
        verify(repository).findById(id);
    }

    @Test
    void testBuscarPorId_NaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        MaterialMonitoria resultado = service.buscarPorId(id);

        assertNull(resultado);
        verify(repository).findById(id);
    }

    @Test
    void testListarPorMonitoria() {
        UUID monitoriaId = UUID.randomUUID();
        List<MaterialMonitoria> materiais = List.of(gerarMaterial());
        when(repository.findByMonitoriaId(monitoriaId)).thenReturn(materiais);

        List<MaterialMonitoria> resultado = service.listarPorMonitoria(monitoriaId);

        assertEquals(materiais, resultado);
        verify(repository).findByMonitoriaId(monitoriaId);
    }

    @Test
    void testExcluirMaterial() {
        UUID id = UUID.randomUUID();

        service.excluir(id);

        verify(repository).deleteById(id);
    }

    // Método auxiliar para criar material fictício
    private MaterialMonitoria gerarMaterial() {
        return MaterialMonitoria.builder()
                .id(UUID.randomUUID())
                .titulo("Aula de Álgebra")
                .link("https://drive.exemplo.com/algebra.pdf")
                .arquivo("algebra.pdf")
                .monitoria(Monitoria.builder().id(UUID.randomUUID()).build())
                .dataEnvio(LocalDate.now())
                .build();
    }
}