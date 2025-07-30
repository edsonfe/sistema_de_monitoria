package br.ufma.monitoria.exceptions;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatus(ResponseStatusException ex) {
        String reason = ex.getReason();
        String message = (reason != null) ? reason : ex.getMessage();

        return ResponseEntity.status(ex.getStatusCode()).body(message);

    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<?> handleCredenciaisException(CredenciaisInvalidasException ex) {
        return ResponseEntity.status(401)
                .body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500)
                .body(Map.of("mensagem", "Erro inesperado."));
    }
}
