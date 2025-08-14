package br.ufma.monitoria.backend.service;

import br.ufma.monitoria.backend.model.Avaliacao;
import br.ufma.monitoria.backend.repository.AvaliacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    @Transactional
    public Avaliacao registrarAvaliacao(Avaliacao avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }

    public Avaliacao buscarPorId(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));
    }

    public List<Avaliacao> listarPorSessao(Long sessaoId) {
        return avaliacaoRepository.findBySessaoAgendada_SessaoId(sessaoId);
    }

    public List<Avaliacao> listarPorAluno(Long alunoId) {
        return avaliacaoRepository.findByAluno_UsuarioId(alunoId);
    }

    public double calcularMediaEstrelasPorSessao(Long sessaoId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findBySessaoAgendada_SessaoIdAndEstrelasNotNull(sessaoId);

        OptionalDouble media = avaliacoes.stream()
                .mapToInt(Avaliacao::getEstrelas)
                .average();

        return media.orElse(0.0);
    }

    @Transactional
    public void excluirAvaliacao(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Avaliação não encontrada para exclusão");
        }
        avaliacaoRepository.deleteById(id);
    }
}
