package br.ufma.monitoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessao_monitoria")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoMonitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "monitoria_id", nullable = false)
    private Monitoria monitoria;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(columnDefinition = "TEXT")
    private String tema;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_sessao", nullable = false)
    private StatusSessao status;

}
