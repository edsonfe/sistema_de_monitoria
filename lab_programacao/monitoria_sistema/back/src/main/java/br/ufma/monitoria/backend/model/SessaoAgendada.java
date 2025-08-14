package br.ufma.monitoria.backend.model;

import java.time.LocalDateTime;
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
@Table(name = "sessao_agendada")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SessaoAgendada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessaoId;

    @Column(nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSessao status;

    // ---------- RELACIONAMENTOS ----------

    @ManyToOne
    @JoinColumn(name = "monitoria_id", nullable = false)
    private Monitoria monitoria;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    @OneToMany(mappedBy = "sessaoAgendada", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Material> materiais = new ArrayList<>();

    @OneToMany(mappedBy = "sessaoAgendada", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Mensagem> mensagens = new ArrayList<>();

    @OneToMany(mappedBy = "sessaoAgendada", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Avaliacao> avaliacoes = new ArrayList<>();
}
