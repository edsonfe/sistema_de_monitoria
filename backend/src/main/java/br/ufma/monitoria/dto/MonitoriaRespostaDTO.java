package br.ufma.monitoria.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoriaRespostaDTO {
    private UUID id;
    private String disciplina;
    private String curso;
    private String codigo;
    private String link;
    private String observacoes;
    private UUID monitorId;
}
