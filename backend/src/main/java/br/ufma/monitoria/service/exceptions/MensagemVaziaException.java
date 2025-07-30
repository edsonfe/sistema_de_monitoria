package br.ufma.monitoria.service.exceptions;

public class MensagemVaziaException extends RuntimeException {
    public MensagemVaziaException(String mensagem) {
        super(mensagem);
    }
}