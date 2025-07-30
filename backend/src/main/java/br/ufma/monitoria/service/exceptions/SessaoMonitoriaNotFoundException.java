package br.ufma.monitoria.service.exceptions;

public class SessaoMonitoriaNotFoundException extends RuntimeException {
    public SessaoMonitoriaNotFoundException(String mensagem) {
        super(mensagem);
    }
}