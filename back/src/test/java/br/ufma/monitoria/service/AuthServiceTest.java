package br.ufma.monitoria.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.service.contract.AuthServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthServiceInterface authService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveAutenticarUsuarioComCredenciaisValidas() {
        Usuario u = Usuario.builder()
            .nome("João")
            .email("joao@email.com")
            .senha("senha123")
            .tipo(TipoUsuario.ALUNO)
            .build();

        usuarioRepository.save(u);

        boolean resultado = authService.efetuarLogin("joao@email.com", "senha123");
        Assertions.assertTrue(resultado);
    }

    @Test
    void deveLancarErroParaEmailInexistente() {
        RegraNegocioRuntime ex = Assertions.assertThrows(RegraNegocioRuntime.class, () -> {
            authService.efetuarLogin("invalido@email.com", "qualquer");
        });

        Assertions.assertEquals("Erro de autenticação: Email inválido.", ex.getMessage());
    }

    @Test
    void deveLancarErroParaSenhaIncorreta() {
        Usuario u = Usuario.builder()
            .nome("Ana")
            .email("ana@email.com")
            .senha("senhaCorreta")
            .tipo(TipoUsuario.MONITOR)
            .build();

        usuarioRepository.save(u);

        RegraNegocioRuntime ex = Assertions.assertThrows(RegraNegocioRuntime.class, () -> {
            authService.efetuarLogin("ana@email.com", "senhaErrada");
        });

        Assertions.assertEquals("Erro de autenticação: Senha incorreta.", ex.getMessage());
    }
}
