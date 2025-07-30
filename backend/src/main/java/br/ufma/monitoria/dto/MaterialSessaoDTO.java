package br.ufma.monitoria.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialSessaoDTO {
    private UUID id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    private String link;
    private String arquivo;

    @NotNull(message = "A sessão é obrigatória")
    private UUID sessaoMonitoriaId;

    @NotNull(message = "A data de envio é obrigatória")
    private LocalDate dataEnvio;
}
