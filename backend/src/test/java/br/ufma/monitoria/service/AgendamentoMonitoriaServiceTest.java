/* 
package br.ufma.monitoria.service;

import br.ufma.monitoria.model.AgendamentoMonitoria;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.repository.AgendamentoMonitoriaRepository;
import br.ufma.monitoria.repository.MonitoriaRepository;
import br.ufma.monitoria.service.impl.AgendamentoMonitoriaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AgendamentoMonitoriaServiceTest {
    private AgendamentoMonitoriaRepository agendamentoRepo;
    private MonitoriaRepository monitoriaRepo;
    private AgendamentoMonitoriaService service;
    private UUID monitoriaId;
    private LocalDate dia;
    private LocalTime horario;

    @BeforeEach
    void setUp() {
        agendamentoRepo = mock(AgendamentoMonitoriaRepository.class);
        monitoriaRepo = mock(MonitoriaRepository.class);
        service = new AgendamentoMonitoriaService(agendamentoRepo, monitoriaRepo);
        monitoriaId = UUID.randomUUID();
        dia = LocalDate.of(2025, 7, 28);
        horario = LocalTime.of(14, 0);
    }

    @Test
    void deveCriarAgendamentoComSucesso() {
        Monitoria monitoria = new Monitoria();
        when(monitoriaRepo.findById(monitoriaId)).thenReturn(Optional.of(monitoria));
        when(agendamentoRepo.existeAgendamento(monitoriaId, dia, horario))
                .thenReturn(Optional.empty());
        AgendamentoMonitoria novo = AgendamentoMonitoria.builder().monitoria(monitoria).dia(dia).horario(horario)
                .build();
        when(agendamentoRepo.save(any())).thenReturn(novo);
        AgendamentoMonitoria resultado = service.criarAgendamento(monitoriaId, dia, horario);
        assertEquals(dia, resultado.getDia());
        assertEquals(horario, resultado.getHorario());
        assertEquals(monitoria, resultado.getMonitoria());
    }

    @Test
    void deveLancarExcecaoAoCriarAgendamentoDuplicado() {
        when(agendamentoRepo.existeAgendamento(monitoriaId, dia, horario))
                .thenReturn(Optional.of(new AgendamentoMonitoria()));
        assertThrows(IllegalArgumentException.class, () -> service.criarAgendamento(monitoriaId, dia, horario));
    }

    @Test
    void deveLancarExcecaoSeMonitoriaNaoExiste() {
        when(agendamentoRepo.existeAgendamento(monitoriaId, dia, horario))
                .thenReturn(Optional.empty());
        when(monitoriaRepo.findById(monitoriaId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.criarAgendamento(monitoriaId, dia, horario));
    }

    @Test
    void deveBuscarAgendamentosPorMonitoria() {
        List<AgendamentoMonitoria> lista = List.of(new AgendamentoMonitoria());
        when(agendamentoRepo.findByMonitoriaId(monitoriaId)).thenReturn(lista);
        List<AgendamentoMonitoria> resultado = service.buscarPorMonitoria(monitoriaId);
        assertEquals(1, resultado.size());
    }

    @Test
    void deveBuscarAgendamentosPorDia() {
        List<AgendamentoMonitoria> lista = List.of(new AgendamentoMonitoria());
        when(agendamentoRepo.findByDia(dia)).thenReturn(lista);
        List<AgendamentoMonitoria> resultado = service.buscarPorDia(dia);
        assertEquals(1, resultado.size());
    }

    @Test
    void deveVerificarSeExisteAgendamento() {
        when(agendamentoRepo.existeAgendamento(monitoriaId, dia, horario))
                .thenReturn(Optional.of(new AgendamentoMonitoria()));
        assertTrue(service.existeAgendamento(monitoriaId, dia, horario));
    }

    @Test
    void deveRetornarFalseSeNaoExisteAgendamento() {
        when(agendamentoRepo.existeAgendamento(monitoriaId, dia, horario))
                .thenReturn(Optional.empty());
        assertFalse(service.existeAgendamento(monitoriaId, dia, horario));
    }
}
    */