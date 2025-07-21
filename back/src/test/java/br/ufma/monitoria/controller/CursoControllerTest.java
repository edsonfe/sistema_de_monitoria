package br.ufma.monitoria.controller;

import br.ufma.monitoria.model.Curso;
import br.ufma.monitoria.repository.CursoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CursoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CursoRepository cursoRepository;

    private String getUrl(String path) {
        return "http://localhost:" + port + "/cursos" + path;
    }

    /*
    @BeforeEach
    void limparDados() {
        cursoRepository.deleteAll();
    }
    */

    @Test
    void deveCriarCursoComSucesso() {
        Curso curso = Curso.builder().nome("Engenharia Civil").build();

        HttpEntity<Curso> request = new HttpEntity<>(curso);
        ResponseEntity<Curso> response = restTemplate.postForEntity(getUrl(""), request, Curso.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Curso body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals("Engenharia Civil", body.getNome());
    }

    @Test
    void deveListarCursos() {
        cursoRepository.save(Curso.builder().nome("Medicina").build());
        cursoRepository.save(Curso.builder().nome("Veterinária").build());

        ResponseEntity<Curso[]> response = restTemplate.getForEntity(getUrl(""), Curso[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Curso[] cursos = response.getBody();
        Assertions.assertNotNull(cursos);
        Assertions.assertTrue(cursos.length >= 2);
    }

    @Test
    void deveBuscarCursoPorId() {
        Curso salvo = cursoRepository.save(Curso.builder().nome("Design Gráfico").build());

        ResponseEntity<Curso> response = restTemplate.getForEntity(getUrl("/" + salvo.getId()), Curso.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Curso body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals("Design Gráfico", body.getNome());
    }

    @Test
    void deveRetornarErroAoBuscarCursoInexistente() {
        ResponseEntity<String> response = restTemplate.getForEntity(getUrl("/9999"), String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        String mensagem = response.getBody();
        Assertions.assertNotNull(mensagem);
        Assertions.assertTrue(mensagem.contains("Curso não encontrado"));
    }
}
