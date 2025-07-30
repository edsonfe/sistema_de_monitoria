package br.ufma.monitoria.model;

import java.time.LocalDate;
import java.util.UUID;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "material_monitoria")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialMonitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String link;

    @Column(columnDefinition = "TEXT")
    private String arquivo;

    @ManyToOne
    @JoinColumn(name = "monitoria_id", nullable = false)
    private Monitoria monitoria;

    @Column(name = "data_envio", nullable = false)
    private LocalDate dataEnvio;
}
