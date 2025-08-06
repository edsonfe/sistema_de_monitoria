package br.ufma.monitoria.backend.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.ufma.monitoria.backend.model.Curso;
import br.ufma.monitoria.backend.model.Monitoria;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.repository.MonitoriaRepository;

class MonitoriaServiceTest {

    @Mock
    private MonitoriaRepository monitoriaRepository;

    @InjectMocks
    private MonitoriaService monitoriaService;

    private Monitoria monitoria;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Usuario monitor = Usuario.builder().usuarioId(1L).nome("João").build();
        Curso curso = Curso.builder().cursoId(1L).nome("Computação").codigo("CC001").build();

        monitoria = Monitoria.builder()
                .monitoriaId(1L)
                .disciplina("Estrutura de Dados")
                .codigoDisciplina("ED123")
                .monitor(monitor)
                .curso(curso)
                .build();
    }

    @Test
    void deveCriarMonitoriaComSucesso() {
        when(monitoriaRepository.save(monitoria)).thenReturn(monitoria);

        Monitoria salva = monitoriaService.criarMonitoria(monitoria);

        assertThat(salva).isNotNull();
        assertThat(salva.getDisciplina()).isEqualTo("Estrutura de Dados");
    }

    @Test
    void deveBuscarMonitoriaPorId() {
        when(monitoriaRepository.findById(1L)).thenReturn(Optional.of(monitoria));

        Monitoria encontrada = monitoriaService.buscarPorId(1L);

        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getCodigoDisciplina()).isEqualTo("ED123");
    }

    @Test
    void deveFalharAoBuscarMonitoriaInexistente() {
        when(monitoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> monitoriaService.buscarPorId(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Monitoria não encontrada");
    }

    @Test
    void deveListarMonitoriasPorCurso() {
        when(monitoriaRepository.findByCurso_CursoId(1L)).thenReturn(List.of(monitoria));

        List<Monitoria> resultado = monitoriaService.buscarPorCurso(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getDisciplina()).isEqualTo("Estrutura de Dados");
    }

    @Test
    void deveAtualizarMonitoriaComSucesso() {
        when(monitoriaRepository.existsById(1L)).thenReturn(true);
        when(monitoriaRepository.save(monitoria)).thenReturn(monitoria);

        Monitoria atualizada = monitoriaService.atualizarMonitoria(monitoria);

        assertThat(atualizada).isNotNull();
        assertThat(atualizada.getDisciplina()).isEqualTo("Estrutura de Dados");
    }

    @Test
    void deveFalharAtualizacaoMonitoriaInexistente() {
        when(monitoriaRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> monitoriaService.atualizarMonitoria(monitoria))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Monitoria não encontrada para atualização");
    }

    @Test
    void deveRemoverMonitoriaComSucesso() {
        when(monitoriaRepository.existsById(1L)).thenReturn(true);

        monitoriaService.removerMonitoria(1L);

        verify(monitoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveFalharRemocaoMonitoriaInexistente() {
        when(monitoriaRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> monitoriaService.removerMonitoria(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Monitoria não encontrada para exclusão");
    }
}
