package br.ufma.monitoria.controller;

import br.ufma.monitoria.dto.MensagemChatDTO;
import br.ufma.monitoria.dto.MensagemChatMapper;
import br.ufma.monitoria.model.MensagemChat;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.SessaoMonitoriaRepository;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.service.contract.MensagemChatServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mensagens")
@RequiredArgsConstructor
public class MensagemChatController {

    private final MensagemChatServiceInterface mensagemService;
    private final SessaoMonitoriaRepository sessaoRepo;
    private final UsuarioRepository usuarioRepo;
    private final MensagemChatMapper mapper;

    @PostMapping
    public ResponseEntity<MensagemChatDTO> criar(@RequestBody @Valid MensagemChatDTO dto) {
        Usuario autor = usuarioRepo.findById(dto.getAutorId())
                .orElseThrow(() -> new IllegalArgumentException("Autor da mensagem não encontrado"));
        SessaoMonitoria sessao = sessaoRepo.findById(dto.getSessaoMonitoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Sessão de monitoria não encontrada"));

        MensagemChat mensagem = mapper.toEntity(dto, autor, sessao);
        MensagemChat salva = mensagemService.salvarMensagem(mensagem);
        return ResponseEntity.ok(mapper.toDTO(salva));
    }

    @GetMapping("/sessao/{sessaoId}")
    public ResponseEntity<List<MensagemChatDTO>> listarPorSessao(@PathVariable UUID sessaoId) {
        List<MensagemChatDTO> mensagens = mensagemService.listarMensagensPorSessao(sessaoId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(mensagens);
    }
}