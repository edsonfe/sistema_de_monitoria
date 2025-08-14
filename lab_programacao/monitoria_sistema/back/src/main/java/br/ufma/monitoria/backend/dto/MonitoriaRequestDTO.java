package br.ufma.monitoria.backend.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class MonitoriaRequestDTO {

    @NotBlank(message = "Disciplina é obrigatória")
    @Size(max = 100, message = "Disciplina pode ter no máximo 100 caracteres")
    private String disciplina;

    @NotBlank(message = "Código da disciplina é obrigatório")
    @Size(max = 20, message = "Código da disciplina pode ter no máximo 20 caracteres")
    private String codigoDisciplina;

    @NotBlank(message = "Dias da semana são obrigatórios")
    @Size(max = 50, message = "Dias da semana podem ter no máximo 50 caracteres")
    private String diasDaSemana;

    @NotNull(message = "Horário é obrigatório")
    private LocalTime horario;

    @Size(max = 255, message = "Link da sala virtual pode ter no máximo 255 caracteres")
    private String linkSalaVirtual;

    @Size(max = 500, message = "Observações podem ter no máximo 500 caracteres")
    private String observacoes;

    @NotNull(message = "ID do monitor é obrigatório")
    private Long monitorId;

    @NotNull(message = "ID do curso é obrigatório")
    private Long cursoId;
}
