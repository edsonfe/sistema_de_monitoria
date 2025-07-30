package br.ufma.monitoria.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.ufma.monitoria.model.AgendamentoMonitoria;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;

@DataJpaTest
class AgendamentoMonitoriaRepositoryTest {

    @Autowired
    private AgendamentoMonitoriaRepository repository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveTestarConsultasDoRepositorio() {
        // Criar e salvar o monitor
        Usuario monitor = Usuario.builder()
            .nome("Joana")
            .email("joana@ufma.br")
            .senha("senha456")
            .tipo(TipoUsuario.MONITOR)
            .build();
        monitor = usuarioRepository.save(monitor);

        // Criar e salvar a monitoria
        Monitoria monitoria = Monitoria.builder()
            .codigo("MON500")
            .curso("Direito")
            .disciplina("Processo Civil")
            .monitor(monitor)
            .build();
        monitoria = monitoriaRepository.save(monitoria);

        // Criar e salvar o agendamento
        AgendamentoMonitoria agendamento = AgendamentoMonitoria.builder()
            .monitoria(monitoria)
            .dia(LocalDate.of(2025, 7, 31))
            .horario(LocalTime.of(10, 0))
            .build();
        repository.save(agendamento);

        // Verificações
        List<AgendamentoMonitoria> porMonitoria = repository.findByMonitoriaId(monitoria.getId());
        assertEquals(1, porMonitoria.size());
        assertEquals(LocalTime.of(10, 0), porMonitoria.get(0).getHorario());

        List<AgendamentoMonitoria> porDia = repository.findByDia(LocalDate.of(2025, 7, 31));
        assertFalse(porDia.isEmpty());
        assertEquals(monitoria.getId(), porDia.get(0).getMonitoria().getId());

        Optional<AgendamentoMonitoria> opcional = repository.findByMonitoriaIdAndDiaAndHorario(
            monitoria.getId(),
            LocalDate.of(2025, 7, 31),
            LocalTime.of(10, 0)
        );
        assertTrue(opcional.isPresent());
        assertEquals(LocalDate.of(2025, 7, 31), opcional.get().getDia());
    }
}
