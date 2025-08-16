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
public class NotificacaoResponseDTO {
    private Long notificacaoId;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private boolean lida;
    private Long usuarioDestinoId;
    private String usuarioDestinoNome;
    private String link;
}
