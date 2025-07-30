package br.ufma.monitoria.exceptions;

public class MonitoriaNotFoundException extends RuntimeException {
    public MonitoriaNotFoundException(String message) {
        super(message);
    }
}
