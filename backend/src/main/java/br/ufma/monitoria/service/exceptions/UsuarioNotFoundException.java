package br.ufma.monitoria.service.exceptions;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String mensagem) {
        super(mensagem);
    }
}