package br.ufma.monitoria.repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.StatusSessao;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;

@DataJpaTest
class SessaoMonitoriaRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private SessaoMonitoriaRepository sessaoRepository;

    @Test
    void deveFiltrarPorMonitoriaOuAlunoId() {
        // 1. Criar e salvar o aluno
        Usuario aluno = Usuario.builder()
            .nome("Larissa")
            .email("larissa@ufma.br")
            .senha("123456")
            .tipo(TipoUsuario.ALUNO)
            .build();
        usuarioRepository.save(aluno);

        // 2. Criar e salvar o monitor
        Usuario monitor = Usuario.builder()
            .nome("Carlos")
            .email("carlos@ufma.br")
            .senha("654321")
            .tipo(TipoUsuario.MONITOR)
            .build();
        usuarioRepository.save(monitor);

        // 3. Criar e salvar a monitoria
        Monitoria monitoria = Monitoria.builder()
            .codigo("MONX1")
            .curso("Engenharia de Software")
            .monitor(monitor)
            .build();
        monitoriaRepository.save(monitoria);

        // 4. Criar e salvar a sessão de monitoria
        SessaoMonitoria sessao = SessaoMonitoria.builder()
            .monitoria(monitoria)
            .aluno(aluno)
            .dataHora(LocalDateTime.of(2025, 8, 1, 14, 30))
            .tema("Refatoração orientada a testes")
            .status(StatusSessao.CONCLUIDA)
            .build();
        sessaoRepository.save(sessao);

        // 5. Consultar por alunoId
        List<SessaoMonitoria> porAluno = sessaoRepository.findByAlunoId(aluno.getId());
        assertEquals(1, porAluno.size());
        assertEquals("Refatoração orientada a testes", porAluno.get(0).getTema());

        // 6. Consultar por monitoriaId
        List<SessaoMonitoria> porMonitoria = sessaoRepository.findByMonitoriaId(monitoria.getId());
        assertEquals(1, porMonitoria.size());
        assertEquals(aluno.getId(), porMonitoria.get(0).getAluno().getId());
    }
}
