package br.ufma.monitoria.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "monitoria")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Monitoria {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "monitor_id", nullable = false)
    private Usuario monitor;
    @Column(nullable = false)
    private String codigo;
    private String disciplina;
    @Column(nullable = false)
    private String curso;
    @Column(columnDefinition = "TEXT")
    private String link;
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private List<AgendamentoMonitoria> agendamentos;
    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private List<SessaoMonitoria> sessoes;
    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private List<MaterialMonitoria> materiais;
}