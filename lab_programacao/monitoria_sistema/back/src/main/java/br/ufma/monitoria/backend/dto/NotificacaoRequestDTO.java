package br.ufma.monitoria.backend.dto;

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
public class NotificacaoRequestDTO {
    private String mensagem;
    private Long usuarioDestinoId;
    private String link;
}
