package br.ufma.monitoria.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "monitoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Monitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monitoriaId;

    @Column(nullable = false, length = 100)
    private String disciplina;

    @Column(name = "codigo_disciplina", nullable = false, length = 20)
    private String codigoDisciplina;

    @Column(name = "dias_da_semana", length = 50)
    private String diasDaSemana;

    @Column
    private LocalTime horario;

    @Column(name = "link_sala_virtual", length = 255)
    private String linkSalaVirtual;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // ---------- RELACIONAMENTOS ----------

    @ManyToOne
    @JoinColumn(name = "monitor_id", nullable = false)
    private Usuario monitor;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SessaoAgendada> sessoesAgendadas = new ArrayList<>();
}