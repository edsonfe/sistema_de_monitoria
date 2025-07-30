package br.ufma.monitoria.dto;

import org.springframework.stereotype.Component;

import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.Usuario;

@Component
public class SessaoMonitoriaMapper {

    public SessaoMonitoria toEntity(SessaoMonitoriaDTO dto, Monitoria monitoria, Usuario aluno) {
        return SessaoMonitoria.builder()
                .id(dto.getId())
                .monitoria(monitoria)
                .aluno(aluno)
                .dataHora(dto.getDataHora())
                .tema(dto.getTema())
                .status(dto.getStatus())
                .build();
    }

    public SessaoMonitoriaDTO toDTO(SessaoMonitoria entity) {
        if (entity == null || entity.getMonitoria() == null || entity.getAluno() == null) {
            throw new IllegalArgumentException("SessaoMonitoria ou suas dependências estão nulas");
        }

        return SessaoMonitoriaDTO.builder()
                .id(entity.getId())
                .monitoriaId(entity.getMonitoria().getId())
                .alunoId(entity.getAluno().getId())
                .dataHora(entity.getDataHora())
                .tema(entity.getTema())
                .status(entity.getStatus())
                .build();
    }

}
