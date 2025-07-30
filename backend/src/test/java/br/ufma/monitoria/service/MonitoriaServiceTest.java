package br.ufma.monitoria.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.ufma.monitoria.dto.MonitoriaCadastroDTO;
import br.ufma.monitoria.dto.MonitoriaMapper;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.MonitoriaRepository;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.service.impl.MonitoriaService;

class MonitoriaServiceTest {

    @Mock
    private MonitoriaRepository monitoriaRepository;
    
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MonitoriaMapper monitoriaMapper;

    @InjectMocks
    private MonitoriaService monitoriaService;

    private Monitoria monitoriaMock;
    private UUID idMock;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);
        idMock = UUID.randomUUID();

        monitoriaMock = Monitoria.builder()
            .id(idMock)
            .codigo("MON123")
            .curso("Sistemas de Informação")
            .disciplina("Estrutura de Dados")
            .link("https://meet.exemplo.com/monitoria")
            .observacoes("Acompanhar toda semana")
            .build();
    }

    @Test
    void testCriarMonitoria() {
        MonitoriaCadastroDTO dtoMock = MonitoriaCadastroDTO.builder()
            .codigo("MON123")
            .curso("Sistemas de Informação")
            .disciplina("Estrutura de Dados")
            .link("https://meet.exemplo.com/monitoria")
            .observacoes("Acompanhar toda semana")
            .monitorId(UUID.randomUUID())
            .build();

        Usuario usuarioMock = new Usuario();

        when(usuarioRepository.findById(dtoMock.getMonitorId())).thenReturn(Optional.of(usuarioMock));
        when(monitoriaMapper.toEntity(dtoMock, usuarioMock)).thenReturn(monitoriaMock);
        when(monitoriaRepository.save(any(Monitoria.class))).thenReturn(monitoriaMock);

        Monitoria resultado = monitoriaService.criar(dtoMock);

        assertNotNull(resultado);
        assertEquals("MON123", resultado.getCodigo());
        verify(monitoriaMapper).toEntity(dtoMock, usuarioMock);
        verify(monitoriaRepository).save(any(Monitoria.class));
    }

    @Test
    void testBuscarPorId() {
        when(monitoriaRepository.findById(idMock)).thenReturn(Optional.of(monitoriaMock));

        Optional<Monitoria> resultado = monitoriaService.buscarPorId(idMock);

        assertTrue(resultado.isPresent());
        assertEquals("Estrutura de Dados", resultado.get().getDisciplina());
        verify(monitoriaRepository).findById(idMock);
    }

    @Test
    void testListarPorCurso() {
        when(monitoriaRepository.findByCurso("Sistemas de Informação")).thenReturn(List.of(monitoriaMock));

        List<Monitoria> resultado = monitoriaService.listarPorCurso("Sistemas de Informação");

        assertEquals(1, resultado.size());
        verify(monitoriaRepository).findByCurso("Sistemas de Informação");
    }

    @Test
    void testListarPorDisciplina() {
        when(monitoriaRepository.findByDisciplina("Estrutura de Dados")).thenReturn(List.of(monitoriaMock));

        List<Monitoria> resultado = monitoriaService.listarPorDisciplina("Estrutura de Dados");

        assertEquals(1, resultado.size());
        verify(monitoriaRepository).findByDisciplina("Estrutura de Dados");
    }

    @Test
    void testDeletarMonitoria() {
        doNothing().when(monitoriaRepository).deleteById(idMock);

        monitoriaService.deletar(idMock);

        verify(monitoriaRepository).deleteById(idMock);
    }
}
