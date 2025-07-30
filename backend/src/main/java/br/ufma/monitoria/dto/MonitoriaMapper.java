package br.ufma.monitoria.dto;

import org.springframework.stereotype.Component;

import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.Usuario;

@Component
public class MonitoriaMapper {

    public Monitoria toEntity(MonitoriaCadastroDTO dto, Usuario monitor) {
        return Monitoria.builder()
                .disciplina(dto.getDisciplina())
                .curso(dto.getCurso())
                .codigo(dto.getCodigo())
                .link(dto.getLink())
                .observacoes(dto.getObservacoes())
                .monitor(monitor)
                .build();
    }

    public MonitoriaRespostaDTO toDTO(Monitoria entity) {
        return MonitoriaRespostaDTO.builder()
                .id(entity.getId())
                .disciplina(entity.getDisciplina())
                .curso(entity.getCurso())
                .codigo(entity.getCodigo())
                .link(entity.getLink())
                .observacoes(entity.getObservacoes())
                .monitorId(entity.getMonitor().getId())
                .build();
    }
}
