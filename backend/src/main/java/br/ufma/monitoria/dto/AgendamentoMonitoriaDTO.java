package br.ufma.monitoria.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoMonitoriaDTO {
    private UUID id;
    @NotNull(message = "A monitoria é obrigatória")
    private UUID monitoriaId;
    @NotNull(message = "O dia é obrigatório")
    private LocalDate dia;
    @NotNull(message = "O horário é obrigatório")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horario;
}