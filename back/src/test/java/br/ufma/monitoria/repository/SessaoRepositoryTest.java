package br.ufma.monitoria.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.ufma.monitoria.model.Sessao;

@SpringBootTest
public class SessaoRepositoryTest {

    @Autowired
    SessaoRepository sessaoRepository;

    @Test
    void deveSalvarSessao() {
        Sessao sessao = Sessao.builder().status("ATIVA").build();
        sessaoRepository.save(sessao);
        assertNotNull(sessao.getId());
    }
}
