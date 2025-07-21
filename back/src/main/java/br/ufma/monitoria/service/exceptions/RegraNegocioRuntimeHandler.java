package br.ufma.monitoria.service.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RegraNegocioRuntimeHandler {
    @ExceptionHandler(RegraNegocioRuntime.class)
    public ResponseEntity<String> handle(RegraNegocioRuntime ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
