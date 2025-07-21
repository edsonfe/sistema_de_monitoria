package br.ufma.monitoria.controller;

import br.ufma.monitoria.model.*;
import br.ufma.monitoria.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UsuarioControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    private String getUrl(String path) {
        return "http://localhost:" + port + "/usuarios" + path;
    }

    /*
    @BeforeEach
    void limparBanco() {
        usuarioRepository.deleteAll();
        disciplinaRepository.deleteAll();
        cursoRepository.deleteAll();
    }
    */

    @Test
    void deveCriarUsuarioMonitorComSucesso() {
        Curso curso = cursoRepository.save(Curso.builder().nome("Física").build());
        Disciplina disciplina = disciplinaRepository.save(Disciplina.builder().nome("Ondas").build());

        Usuario usuario = Usuario.builder()
            .nome("Carlos")
            .email("carlos@email.com")
            .senha("senha123")
            .matricula("202312345")
            .dataNascimento(LocalDateTime.of(2000, 5, 15, 0, 0))
            .codigoVerificacao("abc123")
            .tipo(TipoUsuario.MONITOR)
            .curso(curso)
            .disciplina(disciplina)
            .build();

        HttpEntity<Usuario> request = new HttpEntity<>(usuario);
        ResponseEntity<Usuario> response = restTemplate.postForEntity(getUrl(""), request, Usuario.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Usuario body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals("Carlos", body.getNome());
        Assertions.assertEquals("Física", body.getCurso().getNome());
        Assertions.assertEquals("Ondas", body.getDisciplina().getNome());
    }

    @Test
    void deveListarUsuarios() {
        Curso curso = cursoRepository.save(Curso.builder().nome("Direito").build());

        Usuario u1 = Usuario.builder()
            .nome("Ana").email("ana@email.com").senha("123456")
            .tipo(TipoUsuario.ALUNO).curso(curso).build();

        Usuario u2 = Usuario.builder()
            .nome("Rafael").email("rafael@email.com").senha("654321")
            .tipo(TipoUsuario.ALUNO).curso(curso).build();

        usuarioRepository.saveAll(List.of(u1, u2));

        ResponseEntity<Usuario[]> response = restTemplate.getForEntity(getUrl(""), Usuario[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Usuario[] usuarios = response.getBody();
        Assertions.assertNotNull(usuarios);
        Assertions.assertTrue(usuarios.length >= 2);
    }

    @Test
    void deveBuscarUsuarioPorId() {
        Curso curso = cursoRepository.save(Curso.builder().nome("Química").build());

        Usuario salvo = usuarioRepository.save(
            Usuario.builder().nome("Lucas").email("lucas@email.com").senha("lucas123")
            .tipo(TipoUsuario.ALUNO).curso(curso).build()
        );

        ResponseEntity<Usuario> response = restTemplate.getForEntity(getUrl("/" + salvo.getId()), Usuario.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Usuario body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals("Lucas", body.getNome());
    }

    @Test
    void deveRetornarErroAoBuscarUsuarioInexistente() {
        ResponseEntity<String> response = restTemplate.getForEntity(getUrl("/9999"), String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        String mensagem = response.getBody();
        Assertions.assertNotNull(mensagem);
        Assertions.assertTrue(mensagem.contains("Usuário não encontrado"));
    }
}
