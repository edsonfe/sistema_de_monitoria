package br.ufma.monitoria.controller;

import br.ufma.monitoria.dto.AvaliacaoDTO;
import br.ufma.monitoria.dto.AvaliacaoMapper;
import br.ufma.monitoria.model.Avaliacao;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.repository.SessaoMonitoriaRepository;
import br.ufma.monitoria.service.contract.AvaliacaoServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoServiceInterface avaliacaoService;
    private final SessaoMonitoriaRepository sessaoRepo;
    private final AvaliacaoMapper mapper;

    @PostMapping
    public ResponseEntity<AvaliacaoDTO> criar(@RequestBody @Valid AvaliacaoDTO dto) {
        SessaoMonitoria sessao = sessaoRepo.findById(dto.getSessaoMonitoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Sessão de monitoria não encontrada"));

        dto.setData(LocalDateTime.now());
        Avaliacao avaliacao = mapper.toEntity(dto, sessao);
        Avaliacao salva = avaliacaoService.salvarAvaliacao(avaliacao);
        return ResponseEntity.ok(mapper.toDTO(salva));
    }

    @GetMapping("/sessao/{sessaoId}")
    public ResponseEntity<AvaliacaoDTO> buscarPorSessao(@PathVariable UUID sessaoId) {
        Optional<Avaliacao> avaliacao = avaliacaoService.buscarPorSessaoMonitoriaId(sessaoId);
        return avaliacao
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/monitoria/{monitoriaId}/media")
    public ResponseEntity<Double> calcularMedia(@PathVariable UUID monitoriaId) {
        Double media = avaliacaoService.calcularMediaPorMonitoria(monitoriaId);
        return media == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(media);
    }
}
