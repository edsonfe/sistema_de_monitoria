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
public class SessaoAgendadaRequestDTO {
    private Long alunoId;
    private Long monitoriaId;
    private LocalDateTime data;
    private StatusSessao status;
}