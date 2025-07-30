package br.ufma.monitoria.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.dto.AgendamentoMonitoriaDTO;
import br.ufma.monitoria.dto.AgendamentoMonitoriaMapper;
import br.ufma.monitoria.service.contract.AgendamentoMonitoriaServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoMonitoriaController {
    private final AgendamentoMonitoriaServiceInterface agendamentoService;
    private final AgendamentoMonitoriaMapper mapper;

    @PostMapping
    public ResponseEntity<AgendamentoMonitoriaDTO> criarAgendamento(@Valid @RequestBody AgendamentoMonitoriaDTO dto) {
        var agendamento = agendamentoService.criarAgendamento(dto.getMonitoriaId(), dto.getDia(), dto.getHorario());
        return ResponseEntity.ok(mapper.toDTO(agendamento));
    }

    @GetMapping("/monitoria/{monitoriaId}")
    public ResponseEntity<List<AgendamentoMonitoriaDTO>> buscarPorMonitoria(@PathVariable UUID monitoriaId) {
        List<AgendamentoMonitoriaDTO> lista = agendamentoService.buscarPorMonitoria(monitoriaId).stream()
                .map(mapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/dia/{dia}")
    public ResponseEntity<List<AgendamentoMonitoriaDTO>> buscarPorDia(@PathVariable LocalDate dia) {
        List<AgendamentoMonitoriaDTO> lista = agendamentoService.buscarPorDia(dia).stream().map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeAgendamento(@RequestParam UUID monitoriaId, @RequestParam LocalDate dia,
            @RequestParam LocalTime horario) {
        boolean existe = agendamentoService.existeAgendamento(monitoriaId, dia, horario);
        return ResponseEntity.ok(existe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoMonitoriaDTO> atualizarAgendamento(@PathVariable UUID id,
            @Valid @RequestBody AgendamentoMonitoriaDTO dto) {
        var atualizado = agendamentoService.atualizarAgendamento(id, dto.getMonitoriaId(), dto.getDia(),
                dto.getHorario());
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAgendamento(@PathVariable UUID id) {
        agendamentoService.excluirAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}