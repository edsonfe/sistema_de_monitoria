package br.ufma.monitoria.repository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.ufma.monitoria.model.MaterialSessao;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.StatusSessao;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;
@DataJpaTest
class MaterialSessaoRepositoryTest {

    @Autowired
    private MaterialSessaoRepository materialRepository;

    @Autowired
    private SessaoMonitoriaRepository sessaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Test
    void deveRetornarMateriaisPorSessaoId() {
        // 1. Salvar o monitor
        Usuario monitor = Usuario.builder()
            .nome("Larissa Monitor")
            .email("larissa@ufma.br")
            .senha("123456")
            .tipo(TipoUsuario.MONITOR)
            .build();
        monitor = usuarioRepository.save(monitor);

        // 2. Salvar o aluno
        Usuario aluno = Usuario.builder()
            .nome("Carlos Aluno")
            .email("carlos@ufma.br")
            .senha("654321")
            .tipo(TipoUsuario.ALUNO)
            .build();
        aluno = usuarioRepository.save(aluno);

        // 3. Salvar a monitoria
        Monitoria monitoria = Monitoria.builder()
            .codigo("MON101")
            .curso("Sistemas Distribuídos")
            .monitor(monitor)
            .build();
        monitoria = monitoriaRepository.save(monitoria);

        // 4. Salvar a sessão
        SessaoMonitoria sessao = SessaoMonitoria.builder()
            .monitoria(monitoria)
            .aluno(aluno)
            .dataHora(LocalDate.now().atTime(10, 0))
            .tema("Introdução a APIs REST")
            .status(StatusSessao.CONCLUIDA)
            .build();
        sessao = sessaoRepository.save(sessao);

        // 5. Salvar o material
        MaterialSessao material = MaterialSessao.builder()
            .titulo("Slides de Revisão")
            .sessaoMonitoria(sessao)
            .dataEnvio(LocalDate.now())
            .build();
        materialRepository.save(material);

        // 6. Verificar recuperação
        assertEquals(1, materialRepository.findBySessaoMonitoriaId(sessao.getId()).size());
    }
}
