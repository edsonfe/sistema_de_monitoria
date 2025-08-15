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
public class MaterialResponseDTO {
    private Long materialId;
    private String titulo;
    private String link;
    private Long sessaoId;
    private LocalDateTime dataSessao;
    private String arquivo; // nome do arquivo
    private String arquivoUrl; // URL completa para download/visualização
}
