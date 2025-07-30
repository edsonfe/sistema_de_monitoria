package br.ufma.monitoria.dto;

import org.springframework.stereotype.Component;

import br.ufma.monitoria.model.AgendamentoMonitoria;
import br.ufma.monitoria.model.Monitoria;

@Component
public class AgendamentoMonitoriaMapper {
    public AgendamentoMonitoria toEntity(AgendamentoMonitoriaDTO dto, Monitoria monitoria) {
        return AgendamentoMonitoria.builder().id(dto.getId()).monitoria(monitoria).dia(dto.getDia())
                .horario(dto.getHorario()).build();
    }

    public AgendamentoMonitoriaDTO toDTO(AgendamentoMonitoria entity) {
        return AgendamentoMonitoriaDTO.builder().id(entity.getId()).monitoriaId(entity.getMonitoria().getId())
                .dia(entity.getDia()).horario(entity.getHorario()).build();
    }
}