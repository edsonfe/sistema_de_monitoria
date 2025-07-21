package br.ufma.monitoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "monitoria")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Monitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    private String descricao;
    private String observacoes;
    private String link;

    @ManyToOne
    @JoinColumn(name = "monitor_id")
    private Usuario monitor; // tipo MONITOR

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;

    @ManyToMany
    @JoinTable(
        name = "monitoria_aluno",
        joinColumns = @JoinColumn(name = "monitoria_id"),
        inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private Set<Usuario> alunos; // tipo ALUNO

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private Set<Material> materiais;

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private Set<HorarioMonitoria> horarios;

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private Set<Sessao> sessoes;

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private Set<Atividade> atividades;

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private Set<Comentario> comentarios;
}
