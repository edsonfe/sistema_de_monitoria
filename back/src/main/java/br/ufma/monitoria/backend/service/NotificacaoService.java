package br.ufma.monitoria.backend.service;

import br.ufma.monitoria.backend.model.Notificacao;
import br.ufma.monitoria.backend.repository.NotificacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    /**
     * Registra uma nova notificação
     */
    public Notificacao registrarNotificacao(Notificacao notificacao) {
        return notificacaoRepository.save(notificacao);
    }

    /**
     * Busca notificação pelo ID
     */
    public Notificacao buscarPorId(Long id) {
        return notificacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada com ID " + id));
    }

    /**
     * Lista todas as notificações de um usuário
     */
    public List<Notificacao> listarPorUsuario(Long usuarioId) {
        return notificacaoRepository.findByUsuarioDestino_UsuarioId(usuarioId);
    }

    /**
     * Marca uma notificação como lida
     */
    public Notificacao marcarComoLida(Long id) {
        Notificacao notificacao = buscarPorId(id);
        notificacao.setLida(true);
        return notificacaoRepository.save(notificacao);
    }

    /**
     * Exclui uma notificação
     */
    public void excluirNotificacao(Long id) {
        if (!notificacaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Notificação não encontrada para exclusão");
        }
        notificacaoRepository.deleteById(id);
    }
}
