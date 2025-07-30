package br.ufma.monitoria.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.dto.MonitoriaCadastroDTO;
import br.ufma.monitoria.dto.MonitoriaMapper;
import br.ufma.monitoria.dto.MonitoriaRespostaDTO;
import br.ufma.monitoria.exceptions.MonitoriaNotFoundException;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.service.impl.MonitoriaService;

@RestController
@RequestMapping("/api/monitorias")
public class MonitoriaController {

    @Autowired
    private MonitoriaService monitoriaService;

    @Autowired
    private MonitoriaMapper mapper;

    @PostMapping
    public MonitoriaRespostaDTO criar(@RequestBody MonitoriaCadastroDTO dto) {
        Monitoria monitoria = monitoriaService.criar(dto);
        return mapper.toDTO(monitoria);
    }

    @GetMapping("/{id}")
    public MonitoriaRespostaDTO buscarPorId(@PathVariable UUID id) {
        Monitoria monitoria = monitoriaService.buscarPorId(id)
        .orElseThrow(() -> new MonitoriaNotFoundException   ("Monitoria não encontrada"));
        
        return mapper.toDTO(monitoria);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable UUID id) {
        monitoriaService.deletar(id);
    }

    @GetMapping
    public List<MonitoriaRespostaDTO> listarTodas() {
        return monitoriaService.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @GetMapping("/por-curso")
    public List<MonitoriaRespostaDTO> listarPorCurso(@RequestParam String curso) {
        return monitoriaService.listarPorCurso(curso)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @GetMapping("/por-disciplina")
    public List<MonitoriaRespostaDTO> listarPorDisciplina(@RequestParam String disciplina) {
        return monitoriaService.listarPorDisciplina(disciplina)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @GetMapping("/por-monitor/{monitorId}")
    public List<MonitoriaRespostaDTO> listarPorMonitor(@PathVariable UUID monitorId) {
        return monitoriaService.listarPorMonitor(monitorId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}
