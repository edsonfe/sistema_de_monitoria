package br.ufma.monitoria.service.exceptions;

public class MaterialSessaoInvalidoException extends RuntimeException {
    public MaterialSessaoInvalidoException(String message) {
        super(message);
    }
}
