package br.ufma.monitoria.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.ufma.monitoria.model.AgendamentoMonitoria;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.repository.AgendamentoMonitoriaRepository;
import br.ufma.monitoria.repository.MonitoriaRepository;
import br.ufma.monitoria.service.contract.AgendamentoMonitoriaServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendamentoMonitoriaService implements AgendamentoMonitoriaServiceInterface {
    private final AgendamentoMonitoriaRepository agendamentoRepo;
    private final MonitoriaRepository monitoriaRepo;

    @Override
    public AgendamentoMonitoria criarAgendamento(UUID monitoriaId, LocalDate dia, LocalTime horario) {
        if (agendamentoRepo.findByMonitoriaIdAndDiaAndHorario(monitoriaId, dia, horario).isPresent()) {
            throw new IllegalArgumentException("Já existe um agendamento para essa data e horário.");
        }
        Monitoria monitoria = monitoriaRepo.findById(monitoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Monitoria não encontrada"));
        AgendamentoMonitoria agendamento = AgendamentoMonitoria.builder().monitoria(monitoria).dia(dia).horario(horario)
                .build();
        return agendamentoRepo.save(agendamento);
    }

    @Override
    public List<AgendamentoMonitoria> buscarPorMonitoria(UUID monitoriaId) {
        return agendamentoRepo.findByMonitoriaId(monitoriaId);
    }

    @Override
    public List<AgendamentoMonitoria> buscarPorDia(LocalDate dia) {
        return agendamentoRepo.findByDia(dia);
    }

    @Override
    public boolean existeAgendamento(UUID monitoriaId, LocalDate dia, LocalTime horario) {
        return agendamentoRepo.findByMonitoriaIdAndDiaAndHorario(monitoriaId, dia, horario).isPresent();
    }

    @Override
    public AgendamentoMonitoria atualizarAgendamento(UUID id, UUID monitoriaId, LocalDate dia, LocalTime horario) {
        AgendamentoMonitoria existente = agendamentoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
        Monitoria monitoria = monitoriaRepo.findById(monitoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Monitoria não encontrada"));
        existente.setDia(dia);
        existente.setHorario(horario);
        existente.setMonitoria(monitoria);
        return agendamentoRepo.save(existente);
    }

    @Override
    public void excluirAgendamento(UUID id) {
        if (!agendamentoRepo.existsById(id)) {
            throw new EntityNotFoundException("Agendamento não encontrado para exclusão");
        }
        agendamentoRepo.deleteById(id);
    }
}