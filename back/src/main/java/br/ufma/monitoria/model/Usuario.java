package br.ufma.monitoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String email;
    private String senha;
    private String matricula;

    private LocalDateTime dataNascimento;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    private String codigoVerificacao;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "disciplina_id") // para monitores
    private Disciplina disciplina;

    @ManyToMany
    @JoinTable(
        name = "aluno_disciplina",
        joinColumns = @JoinColumn(name = "aluno_id"),
        inverseJoinColumns = @JoinColumn(name = "disciplina_id")
    )
    private Set<Disciplina> disciplinas; // se tipo = ALUNO

    @ManyToMany
    @JoinTable(
        name = "monitoria_aluno",
        joinColumns = @JoinColumn(name = "aluno_id"),
        inverseJoinColumns = @JoinColumn(name = "monitoria_id")
    )
    private Set<Monitoria> monitorias;
}
