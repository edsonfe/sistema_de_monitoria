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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.backend.dto.NotificacaoRequestDTO;
import br.ufma.monitoria.backend.dto.NotificacaoResponseDTO;
import br.ufma.monitoria.backend.model.Notificacao;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.service.NotificacaoService;
import br.ufma.monitoria.backend.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> criarNotificacao(@RequestBody NotificacaoRequestDTO dto) {
        try {
            Usuario usuarioDestino = usuarioService.buscarPorId(dto.getUsuarioDestinoId());

            Notificacao notificacao = Notificacao.builder()
                    .mensagem(dto.getMensagem())
                    .link(dto.getLink()) // 🔗
                    .usuarioDestino(usuarioDestino)
                    .dataCriacao(LocalDateTime.now())
                    .lida(false)
                    .build();

            Notificacao salva = notificacaoService.registrarNotificacao(notificacao);
            return ResponseEntity.ok(toResponseDTO(salva));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Usuário destino não encontrado.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            Notificacao notificacao = notificacaoService.buscarPorId(id);
            return ResponseEntity.ok(toResponseDTO(notificacao));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<NotificacaoResponseDTO> notificacoes = notificacaoService.listarPorUsuario(usuarioId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notificacoes);
    }

    @PutMapping("/{id}/lida")
    public ResponseEntity<NotificacaoResponseDTO> marcarComoLida(@PathVariable Long id) {
        try {
            Notificacao notificacao = notificacaoService.marcarComoLida(id);
            return ResponseEntity.ok(toResponseDTO(notificacao));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            notificacaoService.excluirNotificacao(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Conversão para DTO
    private NotificacaoResponseDTO toResponseDTO(Notificacao notificacao) {
        return NotificacaoResponseDTO.builder()
                .notificacaoId(notificacao.getNotificacaoId())
                .mensagem(notificacao.getMensagem())
                .dataCriacao(notificacao.getDataCriacao())
                .lida(notificacao.getLida())
                .usuarioDestinoId(notificacao.getUsuarioDestino().getUsuarioId())
                .usuarioDestinoNome(notificacao.getUsuarioDestino().getNome())
                .link(notificacao.getLink())
                .build();
    }
}
