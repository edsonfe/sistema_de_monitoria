package br.ufma.monitoria.backend.dto;

import java.time.LocalDateTime;

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
public class MensagemResponseDTO {
    private Long mensagemId;
    private String conteudo;
    private LocalDateTime dataHora;
    private Long autorId;
    private String autorNome;
    private Long sessaoId;
}
