package br.ufma.monitoria.dto;

import org.springframework.stereotype.Component;

import br.ufma.monitoria.model.MaterialSessao;
import br.ufma.monitoria.model.SessaoMonitoria;

@Component
public class MaterialSessaoMapper {

    public MaterialSessao toEntity(MaterialSessaoDTO dto, SessaoMonitoria sessao) {
        return MaterialSessao.builder()
                .id(dto.getId())
                .titulo(dto.getTitulo())
                .link(dto.getLink())
                .arquivo(dto.getArquivo())
                .sessaoMonitoria(sessao)
                .dataEnvio(dto.getDataEnvio())
                .build();
    }

    public MaterialSessaoDTO toDTO(MaterialSessao entity) {
        return MaterialSessaoDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .link(entity.getLink())
                .arquivo(entity.getArquivo())
                .sessaoMonitoriaId(entity.getSessaoMonitoria().getId())
                .dataEnvio(entity.getDataEnvio())
                .build();
    }
}
