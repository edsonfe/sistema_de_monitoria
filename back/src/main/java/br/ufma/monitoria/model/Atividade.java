package br.ufma.monitoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "atividade")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    private String descricao;
    private String linkFormulario;

    @ManyToOne
    @JoinColumn(name = "monitoria_id")
    private Monitoria monitoria;

    @OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL)
    private Set<AtividadeResposta> respostas;
}
