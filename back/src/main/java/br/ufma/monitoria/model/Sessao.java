package br.ufma.monitoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessao")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime horario;

    private String link;

    private String status; // agendada, realizada, cancelada

    @ManyToOne
    @JoinColumn(name = "monitoria_id")
    private Monitoria monitoria;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Usuario aluno; // tipo ALUNO
}
