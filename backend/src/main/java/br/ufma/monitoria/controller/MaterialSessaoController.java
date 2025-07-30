package br.ufma.monitoria.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.dto.MaterialSessaoDTO;
import br.ufma.monitoria.dto.MaterialSessaoMapper;
import br.ufma.monitoria.model.MaterialSessao;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.repository.SessaoMonitoriaRepository;
import br.ufma.monitoria.service.contract.MaterialSessaoServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/material-sessao")
@RequiredArgsConstructor
public class MaterialSessaoController {

    private final MaterialSessaoServiceInterface materialSessaoService;
    private final SessaoMonitoriaRepository sessaoRepo;
    private final MaterialSessaoMapper mapper;

    @PostMapping
    public ResponseEntity<MaterialSessaoDTO> criar(@RequestBody @Valid MaterialSessaoDTO dto) {
        SessaoMonitoria sessao = sessaoRepo.findById(dto.getSessaoMonitoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Sessão de monitoria não encontrada"));

        MaterialSessao material = mapper.toEntity(dto, sessao);
        MaterialSessao salvo = materialSessaoService.salvar(material);
        return ResponseEntity.ok(mapper.toDTO(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialSessaoDTO> buscarPorId(@PathVariable UUID id) {
        MaterialSessao material = materialSessaoService.buscarPorId(id);
        return ResponseEntity.ok(mapper.toDTO(material));
    }

    @GetMapping("/sessao/{sessaoId}")
    public ResponseEntity<List<MaterialSessaoDTO>> buscarPorSessao(@PathVariable UUID sessaoId) {
        List<MaterialSessaoDTO> materiais = materialSessaoService.buscarPorSessao(sessaoId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(materiais);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        materialSessaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
