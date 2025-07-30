package br.ufma.monitoria.dto;

import org.springframework.stereotype.Component;

import br.ufma.monitoria.model.MensagemChat;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.Usuario;

@Component
public class MensagemChatMapper {

    public MensagemChat toEntity(MensagemChatDTO dto, Usuario autor, SessaoMonitoria sessao) {
        return MensagemChat.builder()
                .id(dto.getId())
                .conteudo(dto.getConteudo())
                .dataHora(dto.getDataHora())
                .autor(autor)
                .sessaoMonitoria(sessao)
                .build();
    }

    public MensagemChatDTO toDTO(MensagemChat entity) {
        return MensagemChatDTO.builder()
                .id(entity.getId())
                .conteudo(entity.getConteudo())
                .dataHora(entity.getDataHora())
                .autorId(entity.getAutor().getId())
                .sessaoMonitoriaId(entity.getSessaoMonitoria().getId())
                .build();
    }
}
