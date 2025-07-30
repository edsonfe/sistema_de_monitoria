package br.ufma.monitoria.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agendamento_monitoria")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoMonitoria {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "monitoria_id", nullable = false)
    private Monitoria monitoria;
    @Column(nullable = false)
    @NotNull
    private LocalDate dia;
    @Column(nullable = false)
    @NotNull
    private LocalTime horario;
}