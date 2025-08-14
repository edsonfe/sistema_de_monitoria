package br.ufma.monitoria.backend.service;

import br.ufma.monitoria.backend.model.Mensagem;
import br.ufma.monitoria.backend.repository.MensagemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MensagemService {

    private final MensagemRepository mensagemRepository;

    /**
     * Salva uma nova mensagem com a data de envio atual.
     */
    @Transactional
    public Mensagem enviarMensagem(Mensagem mensagem) {
        if (mensagem.getDataHora() == null) {
            mensagem.setDataHora(LocalDateTime.now());
        }
        return mensagemRepository.save(mensagem);
    }

    /**
     * Busca todas as mensagens de uma sessão em ordem cronológica.
     */
    public List<Mensagem> buscarMensagensPorSessao(Long sessaoId) {
        return mensagemRepository.findBySessaoAgendada_SessaoIdOrderByDataHoraAsc(sessaoId);
    }

    /**
     * Busca todas as mensagens de um autor específico em uma sessão.
     */
    public List<Mensagem> buscarMensagensPorAutorNaSessao(Long sessaoId, Long autorId) {
        return mensagemRepository.findBySessaoAgendada_SessaoIdAndAutor_UsuarioIdOrderByDataHoraAsc(sessaoId, autorId);
    }

    /**
     * Busca uma mensagem pelo ID.
     */
    public Optional<Mensagem> buscarPorId(Long id) {
        return mensagemRepository.findById(id);
    }

    /**
     * Remove uma mensagem.
     */
    @Transactional
    public void deletarMensagem(Long id) {
        mensagemRepository.deleteById(id);
    }
}
