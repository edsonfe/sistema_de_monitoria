package br.ufma.monitoria.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.backend.dto.MaterialRequestDTO;
import br.ufma.monitoria.backend.dto.MaterialResponseDTO;
import br.ufma.monitoria.backend.model.Material;
import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.service.MaterialService;
import br.ufma.monitoria.backend.service.SessaoAgendadaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/materiais")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MaterialController {

    private final MaterialService materialService;
    private final SessaoAgendadaService sessaoAgendadaService;

    @PostMapping
    public ResponseEntity<?> enviarMaterial(@RequestBody MaterialRequestDTO dto) {
        try {
            SessaoAgendada sessao = sessaoAgendadaService.buscarPorId(dto.getSessaoId());

            Material material = Material.builder()
                    .titulo(dto.getTitulo())
                    .linkExterno(dto.getLink())
                    .dataEnvio(LocalDateTime.now())
                    .sessaoAgendada(sessao)
                    .build();

            Material salvo = materialService.enviarMaterial(material);
            return ResponseEntity.ok(toResponseDTO(salvo));

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Sessão não encontrada para envio do material.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            Material material = materialService.buscarPorId(id);
            return ResponseEntity.ok(toResponseDTO(material));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/sessao/{sessaoId}")
    public ResponseEntity<List<MaterialResponseDTO>> listarPorSessao(@PathVariable Long sessaoId) {
        List<MaterialResponseDTO> lista = materialService.listarPorSessao(sessaoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/sessao/{sessaoId}/apos")
    public ResponseEntity<List<MaterialResponseDTO>> listarPorSessaoAposData(
            @PathVariable Long sessaoId,
            @RequestParam LocalDateTime dataEnvio) {

        List<MaterialResponseDTO> lista = materialService
                .listarPorSessaoAposData(sessaoId, dataEnvio)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirMaterial(@PathVariable Long id) {
        try {
            materialService.excluirMaterial(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Conversão para DTO de resposta
    private MaterialResponseDTO toResponseDTO(Material material) {
        return MaterialResponseDTO.builder()
                .materialId(material.getMaterialId())
                .titulo(material.getTitulo())
                .link(material.getLinkExterno())
                .sessaoId(material.getSessaoAgendada().getSessaoId())
                .dataSessao(material.getSessaoAgendada().getData())
                .build();
    }
}
