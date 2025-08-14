package br.ufma.monitoria.backend.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoriaResponseDTO {
    private Long monitoriaId;
    private String disciplina;
    private String codigoDisciplina;
    private String diasDaSemana;
    private LocalTime horario;
    private String linkSalaVirtual;
    private String observacoes;
    private Long monitorId;
    private String monitorNome;
    private Long cursoId;
    private String cursoNome;
}
