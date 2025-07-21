package br.ufma.monitoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "disciplina")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String codigo;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @ManyToMany(mappedBy = "disciplinas")
    private Set<Usuario> alunos;

    @OneToMany(mappedBy = "disciplina")
    private Set<Monitoria> monitorias;

    @OneToMany(mappedBy = "disciplina")
    private Set<Usuario> monitores;
}
