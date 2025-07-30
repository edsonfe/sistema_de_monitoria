package br.ufma.monitoria.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.ufma.monitoria.model.StatusSessao;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoMonitoriaDTO {
    private UUID id;

    @NotNull(message = "Monitoria é obrigatória")
    private UUID monitoriaId;

    @NotNull(message = "Aluno é obrigatório")
    private UUID alunoId;

    @NotNull(message = "Data e hora são obrigatórios")
    private LocalDateTime dataHora;

    private String tema;

    @NotNull(message = "Status é obrigatório")
    private StatusSessao status;
}
