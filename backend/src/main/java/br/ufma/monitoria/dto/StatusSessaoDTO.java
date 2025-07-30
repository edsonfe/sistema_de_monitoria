package br.ufma.monitoria.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.ufma.monitoria.model.StatusSessao;

public class StatusSessaoDTO {
    private UUID sessaoId;
    private StatusSessao statusAtual;
    private LocalDateTime atualizadoEm;

    // Getters e Setters
    public UUID getSessaoId() {
        return sessaoId;
    }

    public void setSessaoId(UUID sessaoId) {
        this.sessaoId = sessaoId;
    }

    public StatusSessao getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(StatusSessao statusAtual) {
        this.statusAtual = statusAtual;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}
