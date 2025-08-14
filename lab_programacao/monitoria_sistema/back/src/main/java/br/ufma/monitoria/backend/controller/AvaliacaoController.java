package br.ufma.monitoria.backend.controller;

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
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.backend.dto.AvaliacaoRequestDTO;
import br.ufma.monitoria.backend.dto.AvaliacaoResponseDTO;
import br.ufma.monitoria.backend.model.Avaliacao;
import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.service.AvaliacaoService;
import br.ufma.monitoria.backend.service.SessaoAgendadaService;
import br.ufma.monitoria.backend.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;
    private final SessaoAgendadaService sessaoService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody AvaliacaoRequestDTO dto) {
        try {
            SessaoAgendada sessao = sessaoService.buscarPorId(dto.getSessaoId());
            Usuario aluno = usuarioService.buscarPorId(dto.getAlunoId());

            Avaliacao avaliacao = Avaliacao.builder()
                    .estrelas(dto.getEstrelas())
                    .comentario(dto.getComentario())
                    .sessaoAgendada(sessao)
                    .aluno(aluno)
                    .build();

            Avaliacao salva = avaliacaoService.registrarAvaliacao(avaliacao);
            return ResponseEntity.ok(toResponseDTO(salva));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Sessão ou aluno não encontrado.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            Avaliacao avaliacao = avaliacaoService.buscarPorId(id);
            return ResponseEntity.ok(toResponseDTO(avaliacao));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/sessao/{sessaoId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorSessao(@PathVariable Long sessaoId) {
        List<AvaliacaoResponseDTO> avaliacoes = avaliacaoService.listarPorSessao(sessaoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorAluno(@PathVariable Long alunoId) {
        List<AvaliacaoResponseDTO> avaliacoes = avaliacaoService.listarPorAluno(alunoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/media/sessao/{sessaoId}")
    public ResponseEntity<Double> calcularMediaPorSessao(@PathVariable Long sessaoId) {
        double media = avaliacaoService.calcularMediaEstrelasPorSessao(sessaoId);
        return ResponseEntity.ok(media);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            avaliacaoService.excluirAvaliacao(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private AvaliacaoResponseDTO toResponseDTO(Avaliacao avaliacao) {
        return AvaliacaoResponseDTO.builder()
                .avaliacaoId(avaliacao.getAvaliacaoId())
                .estrelas(avaliacao.getEstrelas())
                .comentario(avaliacao.getComentario())
                .sessaoId(avaliacao.getSessaoAgendada().getSessaoId())
                .alunoId(avaliacao.getAluno().getUsuarioId())
                .alunoNome(avaliacao.getAluno().getNome())
                .build();
    }
}
