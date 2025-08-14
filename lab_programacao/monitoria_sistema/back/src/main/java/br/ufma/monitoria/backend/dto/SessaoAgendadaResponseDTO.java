package br.ufma.monitoria.backend.dto;

import java.time.LocalDateTime;

import br.ufma.monitoria.backend.model.StatusSessao;
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
public class SessaoAgendadaResponseDTO {
    private Long sessaoId;
    private LocalDateTime data;
    private StatusSessao status;
    private Long alunoId;
    private String alunoNome;
    private Long monitoriaId;
    private String disciplinaMonitoria;
    private String monitorNome;
    private String linkSalaVirtual;

}