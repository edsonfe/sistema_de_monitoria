package br.ufma.monitoria.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.StatusSessao;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.SessaoMonitoriaRepository;
import br.ufma.monitoria.service.impl.SessaoMonitoriaService;

class SessaoMonitoriaServiceTest {

    @Mock
    private SessaoMonitoriaRepository sessaoRepository;

    @InjectMocks
    private SessaoMonitoriaService sessaoService;

    private SessaoMonitoria sessaoMock;
    private UUID idMock;
    private UUID monitoriaId;
    private UUID alunoId;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);
        idMock = UUID.randomUUID();
        monitoriaId = UUID.randomUUID();
        alunoId = UUID.randomUUID();

        sessaoMock = SessaoMonitoria.builder()
                .id(idMock)
                .monitoria(Monitoria.builder().id(monitoriaId).build())
                .aluno(Usuario.builder().id(alunoId).build())
                .dataHora(LocalDateTime.of(2025, 7, 30, 14, 0))
                .tema("Revisão de listas encadeadas")
                .status(StatusSessao.AGENDADA)
                .build();
    }

    @Test
    void testAgendarSessao() {
        when(sessaoRepository.save(any())).thenReturn(sessaoMock);

        SessaoMonitoria resultado = sessaoService.agendarSessao(sessaoMock);

        assertNotNull(resultado);
        assertEquals(StatusSessao.AGENDADA, resultado.getStatus());
        verify(sessaoRepository).save(sessaoMock);
    }

    @Test
    void testBuscarPorId() {
        when(sessaoRepository.findById(idMock)).thenReturn(Optional.of(sessaoMock));

        Optional<SessaoMonitoria> resultado = sessaoService.buscarPorId(idMock);

        assertTrue(resultado.isPresent());
        assertEquals("Revisão de listas encadeadas", resultado.get().getTema());
        verify(sessaoRepository).findById(idMock);
    }

    @Test
    void testListarPorMonitoria() {
        when(sessaoRepository.findByMonitoriaId(monitoriaId)).thenReturn(List.of(sessaoMock));

        List<SessaoMonitoria> resultado = sessaoService.listarPorMonitoria(monitoriaId);

        assertEquals(1, resultado.size());
        verify(sessaoRepository).findByMonitoriaId(monitoriaId);
    }

    @Test
    void testListarPorAluno() {
        when(sessaoRepository.findByAlunoId(alunoId)).thenReturn(List.of(sessaoMock));

        List<SessaoMonitoria> resultado = sessaoService.listarPorAluno(alunoId);

        assertEquals(1, resultado.size());
        verify(sessaoRepository).findByAlunoId(alunoId);
    }

    @Test
    void testAtualizarStatus() {
        when(sessaoRepository.findById(idMock)).thenReturn(Optional.of(sessaoMock));
        when(sessaoRepository.save(any())).thenReturn(sessaoMock);

        SessaoMonitoria resultado = sessaoService.atualizarStatus(idMock, StatusSessao.REALIZADA);

        assertEquals(StatusSessao.REALIZADA, resultado.getStatus());
        verify(sessaoRepository).findById(idMock);
        verify(sessaoRepository).save(sessaoMock);
    }

    @Test
    void testAtualizarStatus_SessaoInexistente() {
        when(sessaoRepository.findById(idMock)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            sessaoService.atualizarStatus(idMock, StatusSessao.CANCELADA);
        });

        verify(sessaoRepository).findById(idMock);
    }
}
