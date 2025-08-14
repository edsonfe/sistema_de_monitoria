package br.ufma.monitoria.backend.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long usuarioId;

  @Column(nullable = false, length = 100)
  private String nome;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(nullable = false, length = 255)
  private String senha;

  @Column(length = 20)
  private String celular;

  @Column(name = "data_nascimento")
  private LocalDate dataNascimento;

  @Column(length = 30, nullable = false)
  private String matricula;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_usuario", nullable = false, length = 10)
  private TipoUsuario tipoUsuario;

  // ---------- RELACIONAMENTOS ----------

  @ManyToOne
  @JoinColumn(name = "curso_id", nullable = false)
  private Curso curso;

  @OneToMany(mappedBy = "monitor", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Monitoria> monitorias = new ArrayList<>();

  @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<SessaoAgendada> sessoesAgendadas = new ArrayList<>();

  @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Mensagem> mensagens = new ArrayList<>();

  @OneToMany(mappedBy = "usuarioDestino", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Notificacao> notificacoes = new ArrayList<>();

  @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Avaliacao> avaliacoes = new ArrayList<>();
}