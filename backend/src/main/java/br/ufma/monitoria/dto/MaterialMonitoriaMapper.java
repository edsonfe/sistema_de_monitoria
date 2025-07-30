package br.ufma.monitoria.dto;

import org.springframework.stereotype.Component;
import br.ufma.monitoria.model.MaterialMonitoria;
import br.ufma.monitoria.model.Monitoria;

@Component
public class MaterialMonitoriaMapper {

    public MaterialMonitoria toEntity(MaterialMonitoriaDTO dto, Monitoria monitoria) {
        return MaterialMonitoria.builder()
                .id(dto.getId())
                .titulo(dto.getTitulo())
                .link(dto.getLink())
                .arquivo(dto.getArquivo())
                .monitoria(monitoria)
                .dataEnvio(dto.getDataEnvio())
                .build();
    }

    public MaterialMonitoriaDTO toDTO(MaterialMonitoria entity) {
        return MaterialMonitoriaDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .link(entity.getLink())
                .arquivo(entity.getArquivo())
                .monitoriaId(entity.getMonitoria().getId())
                .dataEnvio(entity.getDataEnvio())
                .build();
    }
}
