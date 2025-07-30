package br.ufma.monitoria.dto;

import org.springframework.stereotype.Component;

import br.ufma.monitoria.model.Avaliacao;
import br.ufma.monitoria.model.SessaoMonitoria;

@Component
public class AvaliacaoMapper {

    public Avaliacao toEntity(AvaliacaoDTO dto, SessaoMonitoria sessao) {
        return Avaliacao.builder()
                .id(dto.getId())
                .sessaoMonitoria(sessao)
                .nota(dto.getNota())
                .comentario(dto.getComentario())
                .data(dto.getData())
                .build();
    }

    public AvaliacaoDTO toDTO(Avaliacao entity) {
        return AvaliacaoDTO.builder()
                .id(entity.getId())
                .sessaoMonitoriaId(entity.getSessaoMonitoria().getId())
                .nota(entity.getNota())
                .comentario(entity.getComentario())
                .data(entity.getData())
                .build();
    }
}
