package br.ufma.monitoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "material_sessao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialSessao {

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
    @JoinColumn(name = "sessao_monitoria_id", nullable = false)
    private SessaoMonitoria sessaoMonitoria;

    @Column(name = "data_envio", nullable = false)
    private LocalDate dataEnvio;

}
