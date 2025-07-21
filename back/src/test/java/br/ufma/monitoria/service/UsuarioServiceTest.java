package br.ufma.monitoria.service;

import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.service.contract.UsuarioServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UsuarioServiceTest {

    @Autowired
    private UsuarioServiceInterface usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /* //limpar banco
    @BeforeEach
    void limparBanco() {
        usuarioRepository.deleteAll();
    }
    */

    @Test
    void deveSalvarUsuarioValido() {
        Usuario u = Usuario.builder()
            .nome("Maria")
            .email("maria@email.com")
            .senha("123456")
            .tipo(TipoUsuario.ALUNO)
            .build();

        Usuario salvo = usuarioService.salvar(u);
        Assertions.assertNotNull(salvo.getId());
        Assertions.assertEquals("Maria", salvo.getNome());
    }

    @Test
    void deveLancarExcecaoEmailDuplicado() {
        Usuario u = Usuario.builder()
            .nome("Maria")
            .email("duplicado@email.com")
            .senha("123")
            .tipo(TipoUsuario.ALUNO)
            .build();

        usuarioRepository.save(u);

        RegraNegocioRuntime erro = Assertions.assertThrows(RegraNegocioRuntime.class, () -> {
            usuarioService.salvar(u);
        });

        Assertions.assertEquals("Email já cadastrado", erro.getMessage());
    }

    @Test
    void deveBuscarUsuarioPorId() {
        Usuario u = Usuario.builder()
            .nome("Carlos")
            .email("carlos@email.com")
            .senha("senha")
            .tipo(TipoUsuario.MONITOR)
            .build();

        Usuario salvo = usuarioRepository.save(u);

        Usuario buscado = usuarioService.buscarPorId(salvo.getId());
        Assertions.assertEquals("Carlos", buscado.getNome());
    }
}
