package br.ufma.monitoria.repository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.ufma.monitoria.model.MaterialMonitoria;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;

@DataJpaTest
class MaterialMonitoriaRepositoryTest {

    @Autowired
    private MaterialMonitoriaRepository materialRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveBuscarMaterialPorMonitoriaId() {
        // Salvar o monitor
        Usuario monitor = Usuario.builder()
            .nome("Ana Monitor")
            .email("ana@ufma.br")
            .senha("monitor123")
            .tipo(TipoUsuario.MONITOR)
            .build();
        monitor = usuarioRepository.save(monitor);

        // Salvar a monitoria
        Monitoria monitoria = Monitoria.builder()
            .codigo("MON300")
            .curso("Design")
            .monitor(monitor)
            .build();
        monitoria = monitoriaRepository.save(monitoria);

        // Salvar o material
        MaterialMonitoria material = MaterialMonitoria.builder()
            .titulo("Briefing Aula 1")
            .link("https://materiais.ufma.br/design01")
            .arquivo("briefing.pdf")
            .monitoria(monitoria)
            .dataEnvio(LocalDate.of(2025, 7, 28))
            .build();
        materialRepository.save(material);

        // Buscar e validar
        List<MaterialMonitoria> resultado = materialRepository.findByMonitoriaId(monitoria.getId());
        assertEquals(1, resultado.size());
        assertEquals("Briefing Aula 1", resultado.get(0).getTitulo());
    }
}
