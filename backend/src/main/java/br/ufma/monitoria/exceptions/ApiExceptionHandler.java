package br.ufma.monitoria.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.ufma.monitoria.dto.ErrorResponse;
import br.ufma.monitoria.service.exceptions.BusinessException;
import br.ufma.monitoria.service.exceptions.MensagemVaziaException;
import br.ufma.monitoria.service.exceptions.UsuarioNotFoundException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
    }

    @ExceptionHandler(MensagemVaziaException.class)
    public ResponseEntity<ErrorResponse> handleMensagemVazia(MensagemVaziaException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "A mensagem não pode ser vazia.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado.");
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(
            status.value(),
            message,
            LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(error);
    }
}
