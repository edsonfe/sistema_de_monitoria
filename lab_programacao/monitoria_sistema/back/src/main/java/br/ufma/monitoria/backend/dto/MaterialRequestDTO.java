package br.ufma.monitoria.backend.dto;

import jakarta.persistence.Column;
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
public class MaterialRequestDTO {

    private String titulo;
    private String link;
    private Long sessaoId; // Sessão à qual o material pertence
    @Column(length = 255)
    private String arquivo;
}