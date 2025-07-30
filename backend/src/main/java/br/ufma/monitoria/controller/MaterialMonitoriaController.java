package br.ufma.monitoria.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.ufma.monitoria.dto.MaterialMonitoriaDTO;
import br.ufma.monitoria.dto.MaterialMonitoriaMapper;
import br.ufma.monitoria.service.contract.MaterialMonitoriaServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/materiais-monitoria")
@RequiredArgsConstructor
public class MaterialMonitoriaController {

    private final MaterialMonitoriaServiceInterface service;
    private final MaterialMonitoriaMapper mapper;

    @Value("${material.upload.dir}")
    private String caminhoUpload;

    @PostMapping
    public ResponseEntity<MaterialMonitoriaDTO> salvar(@RequestBody @Valid MaterialMonitoriaDTO dto) {
        var entity = mapper.toEntity(dto, null);
        var salvo = service.salvar(entity);
        return ResponseEntity.ok(mapper.toDTO(salvo));
    }

    @PostMapping("/upload")
    public ResponseEntity<MaterialMonitoriaDTO> uploadMaterial(
        @RequestParam("arquivo") MultipartFile arquivo,
        @RequestParam("titulo") String titulo,
        @RequestParam(value = "link", required = false) String link,
        @RequestParam("monitoriaId") UUID monitoriaId,
        @RequestParam("dataEnvio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEnvio
    ) throws IOException {

        // Cria diretório se necessário
        Path pastaUpload = Paths.get(caminhoUpload).toAbsolutePath().normalize();
        Files.createDirectories(pastaUpload);

        // Gera nome seguro pro arquivo
        String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
        Path caminhoCompleto = pastaUpload.resolve(nomeArquivo);
        Files.copy(arquivo.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

        // Monta DTO
        MaterialMonitoriaDTO dto = MaterialMonitoriaDTO.builder()
                .titulo(titulo)
                .link(link)
                .arquivo(caminhoCompleto.toString())
                .monitoriaId(monitoriaId)
                .dataEnvio(dataEnvio)
                .build();

        var salvo = service.salvar(mapper.toEntity(dto, null));
        return ResponseEntity.ok(mapper.toDTO(salvo));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> baixarMaterial(@PathVariable UUID id) throws IOException {
        var material = service.buscarPorId(id);
        if (material == null || material.getArquivo() == null) {
            return ResponseEntity.notFound().build();
        }

        Path caminhoArquivo = Paths.get(material.getArquivo());
        Resource resource = new UrlResource(caminhoArquivo.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        String nomeArquivo = caminhoArquivo.getFileName().toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/monitoria/{monitoriaId}")
    public ResponseEntity<List<MaterialMonitoriaDTO>> listarPorMonitoria(@PathVariable UUID monitoriaId) {
        var lista = service.listarPorMonitoria(monitoriaId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialMonitoriaDTO> buscarPorId(@PathVariable UUID id) {
        var material = service.buscarPorId(id);
        if (material == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toDTO(material));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
