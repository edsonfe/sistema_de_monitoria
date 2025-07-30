package br.ufma.monitoria.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.ufma.monitoria.model.Avaliacao;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.StatusSessao;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;

@DataJpaTest
class AvaliacaoRepositoryTest {

    @Autowired
    private AvaliacaoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private SessaoMonitoriaRepository sessaoRepository;

    @Test
    void deveBuscarAvaliacaoPorSessaoMonitoriaId() {
        // Salvar o aluno
        Usuario aluno = Usuario.builder()
            .nome("Amanda")
            .email("amanda@ufma.br")
            .senha("123456")
            .tipo(TipoUsuario.ALUNO)
            .build();
        aluno = usuarioRepository.save(aluno);

        // Salvar o monitor
        Usuario monitor = Usuario.builder()
            .nome("Luis")
            .email("luis@ufma.br")
            .senha("654321")
            .tipo(TipoUsuario.MONITOR)
            .build();
        monitor = usuarioRepository.save(monitor);

        // Salvar a monitoria
        Monitoria monitoria = Monitoria.builder()
            .codigo("MON999")
            .curso("Engenharia Ambiental")
            .monitor(monitor)
            .build();
        monitoria = monitoriaRepository.save(monitoria);

        // Salvar a sessão
        SessaoMonitoria sessao = SessaoMonitoria.builder()
            .monitoria(monitoria)
            .aluno(aluno)
            .dataHora(LocalDateTime.of(2025, 8, 1, 14, 0))
            .tema("Sustentabilidade")
            .status(StatusSessao.CONCLUIDA)
            .build();
        sessao = sessaoRepository.save(sessao);

        // Salvar a avaliação
        Avaliacao avaliacao = Avaliacao.builder()
            .sessaoMonitoria(sessao)
            .nota(4)
            .comentario("Muito clara e objetiva.")
            .data(LocalDateTime.of(2025, 8, 1, 15, 30))
            .build();
        repository.save(avaliacao);

        // Verificar
        Optional<Avaliacao> resultado = repository.findBySessaoMonitoriaId(sessao.getId());
        assertTrue(resultado.isPresent());
        assertEquals(4, resultado.get().getNota());
        assertEquals("Muito clara e objetiva.", resultado.get().getComentario());
    }
}
