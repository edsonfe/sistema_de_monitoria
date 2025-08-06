package br.ufma.monitoria.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.backend.dto.MensagemRequestDTO;
import br.ufma.monitoria.backend.dto.MensagemResponseDTO;
import br.ufma.monitoria.backend.model.Mensagem;
import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.service.MensagemService;
import br.ufma.monitoria.backend.service.SessaoAgendadaService;
import br.ufma.monitoria.backend.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mensagens")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService mensagemService;
    private final UsuarioService usuarioService;
    private final SessaoAgendadaService sessaoService;

    @PostMapping
    public ResponseEntity<?> enviarMensagem(@RequestBody MensagemRequestDTO dto) {
        try {
            Usuario autor = usuarioService.buscarPorId(dto.getAutorId());
            SessaoAgendada sessao = sessaoService.buscarPorId(dto.getSessaoId());

            Mensagem mensagem = Mensagem.builder()
                    .conteudo(dto.getConteudo())
                    .autor(autor)
                    .sessaoAgendada(sessao)
                    .build();

            Mensagem salva = mensagemService.enviarMensagem(mensagem);
            return ResponseEntity.ok(toResponseDTO(salva));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Autor ou sessão não encontrada.");
        }
    }

    @GetMapping("/sessao/{sessaoId}")
    public ResponseEntity<List<MensagemResponseDTO>> listarPorSessao(@PathVariable Long sessaoId) {
        List<MensagemResponseDTO> mensagens = mensagemService.buscarMensagensPorSessao(sessaoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(mensagens);
    }

    @GetMapping("/sessao/{sessaoId}/autor/{autorId}")
    public ResponseEntity<List<MensagemResponseDTO>> listarPorAutorNaSessao(@PathVariable Long sessaoId,
                                                                           @PathVariable Long autorId) {
        List<MensagemResponseDTO> mensagens = mensagemService.buscarMensagensPorAutorNaSessao(sessaoId, autorId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(mensagens);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMensagem(@PathVariable Long id) {
        mensagemService.deletarMensagem(id);
        return ResponseEntity.noContent().build();
    }

    // Conversão para DTO
    private MensagemResponseDTO toResponseDTO(Mensagem mensagem) {
        return MensagemResponseDTO.builder()
                .mensagemId(mensagem.getMensagemId())
                .conteudo(mensagem.getConteudo())
                .dataHora(mensagem.getDataHora())
                .autorId(mensagem.getAutor().getUsuarioId())
                .autorNome(mensagem.getAutor().getNome())
                .sessaoId(mensagem.getSessaoAgendada().getSessaoId())
                .build();
    }
}
