package br.ufma.monitoria.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;

@DataJpaTest
class MonitoriaRepositoryTest {

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveBuscarMonitoriaPorCursoOuDisciplinaOuMonitor() {
        // Criar e salvar o monitor
        Usuario monitor = Usuario.builder()
            .nome("João Silva")
            .email("joao@ufma.br")
            .senha("senha123")
            .tipo(TipoUsuario.MONITOR)
            .build();
        monitor = usuarioRepository.save(monitor); // Persistência obrigatória

        // Criar a monitoria com o monitor já persistido
        Monitoria monitoria = Monitoria.builder()
            .codigo("MON007")
            .curso("Arquitetura")
            .disciplina("Teoria das Estruturas")
            .monitor(monitor)
            .build();
        monitoriaRepository.save(monitoria);

        // Assertivas
        assertFalse(monitoriaRepository.findByCurso("Arquitetura").isEmpty());
        assertFalse(monitoriaRepository.findByDisciplina("Teoria das Estruturas").isEmpty());
        assertFalse(monitoriaRepository.findByMonitorId(monitor.getId()).isEmpty());
    }
}
