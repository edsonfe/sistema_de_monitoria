package br.ufma.monitoria.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoDTO {
    private UUID id;

    @NotNull(message = "O ID da sessão de monitoria é obrigatório")
    private UUID sessaoMonitoriaId;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 0, message = "A nota mínima é 0")
    @Max(value = 5, message = "A nota máxima é 5")
    private Integer nota;

    @Size(max = 500, message = "O comentário não pode exceder 500 caracteres")
    private String comentario;

    private LocalDateTime data;
}
