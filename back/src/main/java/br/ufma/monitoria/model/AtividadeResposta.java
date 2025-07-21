package br.ufma.monitoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "atividade_resposta")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtividadeResposta {

    @EmbeddedId
    private AtividadeRespostaId id;

    private LocalDateTime dataResposta;

    private String linkResposta;

    @ManyToOne
    @MapsId("atividadeId")
    @JoinColumn(name = "atividade_id")
    private Atividade atividade;

    @ManyToOne
    @MapsId("alunoId")
    @JoinColumn(name = "aluno_id")
    private Usuario aluno; // tipo ALUNO
}
