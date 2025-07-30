package br.ufma.monitoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MensagemChatDTO {
    private UUID id;

    @NotBlank(message = "O conteúdo da mensagem é obrigatório")
    private String conteudo;

    private LocalDateTime dataHora;

    @NotNull(message = "O ID do autor é obrigatório")
    private UUID autorId;

    @NotNull(message = "O ID da sessão de monitoria é obrigatório")
    private UUID sessaoMonitoriaId;
}
