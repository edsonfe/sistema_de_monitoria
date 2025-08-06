package br.ufma.monitoria.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialId;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(length = 255)
    private String arquivo;

    @Column(name = "link_externo", length = 255)
    private String linkExterno;

    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;

    // ---------- RELACIONAMENTO ----------

    @ManyToOne
    @JoinColumn(name = "sessao_id", nullable = false)
    private SessaoAgendada sessaoAgendada;
}