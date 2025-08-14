package br.ufma.monitoria.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avaliacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long avaliacaoId;

    @Column(nullable = false)
    private Integer estrelas;  // Valor entre 1 e 5

    @Column(columnDefinition = "TEXT")
    private String comentario;

    // ---------- RELACIONAMENTOS ----------

    @ManyToOne
    @JoinColumn(name = "sessao_id", nullable = false)
    private SessaoAgendada sessaoAgendada;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;
}