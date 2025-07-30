package br.ufma.monitoria.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class AgendamentoMonitoriaTest {

    private Validator validator;

    @BeforeEach
    void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveAceitarAgendamentoValido() {
        AgendamentoMonitoria agendamento = new AgendamentoMonitoria();
        agendamento.setDia(LocalDate.of(2025, 7, 30));
        agendamento.setHorario(LocalTime.of(10, 30));
        // Simulação de Monitoria vinculada (pode ser mock ou instância direta)
        agendamento.setMonitoria(new Monitoria());

        Set<ConstraintViolation<AgendamentoMonitoria>> violations = validator.validate(agendamento);
        assertTrue(violations.isEmpty(), "Agendamento válido não deveria ter violações.");
    }

    @Test
    void deveFalharSeDiaForNulo() {
        AgendamentoMonitoria agendamento = new AgendamentoMonitoria();
        agendamento.setHorario(LocalTime.of(14, 0));
        agendamento.setMonitoria(new Monitoria());

        Set<ConstraintViolation<AgendamentoMonitoria>> violations = validator.validate(agendamento);
        assertFalse(violations.isEmpty());
    }

    @Test
    void deveFalharSeHorarioForNulo() {
        AgendamentoMonitoria agendamento = new AgendamentoMonitoria();
        agendamento.setDia(LocalDate.of(2025, 7, 30));
        agendamento.setMonitoria(new Monitoria());

        Set<ConstraintViolation<AgendamentoMonitoria>> violations = validator.validate(agendamento);
        assertFalse(violations.isEmpty());
    }

    @Test
    void deveFalharSeMonitoriaForNula() {
        AgendamentoMonitoria agendamento = new AgendamentoMonitoria();
        agendamento.setDia(LocalDate.of(2025, 7, 30));
        agendamento.setHorario(LocalTime.of(14, 0));

        Set<ConstraintViolation<AgendamentoMonitoria>> violations = validator.validate(agendamento);
        assertFalse(violations.isEmpty());
    }
}
