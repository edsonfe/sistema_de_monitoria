package br.ufma.monitoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "curso")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @OneToMany(mappedBy = "curso")
    private Set<Disciplina> disciplinas;

    @OneToMany(mappedBy = "curso")
    private Set<Usuario> usuarios;

    @OneToMany(mappedBy = "curso")
    private Set<Monitoria> monitorias;
}
