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
public class AvaliacaoResponseDTO {
    private Long avaliacaoId;
    private Integer estrelas;
    private String comentario;
    private Long sessaoId;
    private Long alunoId;
    private String alunoNome;
}
