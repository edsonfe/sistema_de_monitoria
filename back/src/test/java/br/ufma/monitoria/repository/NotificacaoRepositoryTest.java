package br.ufma.monitoria.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.ufma.monitoria.model.Notificacao;

@SpringBootTest
public class NotificacaoRepositoryTest {

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @Test
    void deveSalvarNotificacao() {
        Notificacao notificacao = Notificacao.builder()
            .mensagem("Nova sessão disponível!")
            .build();

        notificacaoRepository.save(notificacao);
        assertTrue(notificacaoRepository.count() > 0);
    }
}
