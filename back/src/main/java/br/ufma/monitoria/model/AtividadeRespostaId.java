package br.ufma.monitoria.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtividadeRespostaId implements Serializable {

    private Integer atividadeId;
    private Integer alunoId;
}
