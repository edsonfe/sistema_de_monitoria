package br.ufma.monitoria.service.impl;

import br.ufma.monitoria.model.Avaliacao;
import br.ufma.monitoria.repository.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.ufma.monitoria.service.contract.AvaliacaoServiceInterface;

@Service
@RequiredArgsConstructor
public class AvaliacaoService implements AvaliacaoServiceInterface {

    private final AvaliacaoRepository avaliacaoRepository;

    @Override
    public Avaliacao salvarAvaliacao(Avaliacao avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }

    @Override
    public Optional<Avaliacao> buscarPorSessaoMonitoriaId(UUID sessaoMonitoriaId) {
        return avaliacaoRepository.findBySessaoMonitoriaId(sessaoMonitoriaId);
    }

    @Override
    public Double calcularMediaPorMonitoria(UUID monitoriaId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository
            .findAll()
            .stream()
            .filter(a -> a.getSessaoMonitoria().getMonitoria().getId().equals(monitoriaId))
            .toList();

        return avaliacoes.isEmpty()
            ? null
            : avaliacoes.stream().mapToInt(Avaliacao::getNota).average().orElse(0.0);
    }
}
