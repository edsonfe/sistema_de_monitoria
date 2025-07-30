package br.ufma.monitoria.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoriaCadastroDTO {

    @NotBlank(message = "A disciplina é obrigatória")
    private String disciplina;

    @NotBlank(message = "O curso é obrigatório")
    private String curso;

    private String codigo;
    private String link;
    private String observacoes;

    @NotNull(message = "O monitor é obrigatório")
    private UUID monitorId;
}
