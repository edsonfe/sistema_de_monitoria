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
import br.ufma.monitoria.backend.repository.CursoRepository;

class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        curso = Curso.builder()
                .cursoId(1L)
                .nome("Ciência da Computação")
                .codigo("CC001")
                .build();
    }

    @Test
    void deveCadastrarCursoComSucesso() {
        when(cursoRepository.existsByCodigo("CC001")).thenReturn(false);
        when(cursoRepository.save(curso)).thenReturn(curso);

        Curso salvo = cursoService.cadastrarCurso(curso);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("Ciência da Computação");
        verify(cursoRepository, times(1)).save(curso);
    }

    @Test
    void deveFalharCadastroSeCodigoDuplicado() {
        when(cursoRepository.existsByCodigo("CC001")).thenReturn(true);

        assertThatThrownBy(() -> cursoService.cadastrarCurso(curso))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Código de curso já cadastrado.");
    }

    @Test
    void deveBuscarCursoPorIdComSucesso() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        Curso encontrado = cursoService.buscarPorId(1L);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getCodigo()).isEqualTo("CC001");
    }

    @Test
    void deveLancarExcecaoSeCursoNaoEncontradoPorId() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cursoService.buscarPorId(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Curso não encontrado");
    }

    @Test
    void deveListarTodosCursos() {
        when(cursoRepository.findAll()).thenReturn(List.of(curso));

        List<Curso> cursos = cursoService.listarTodos();

        assertThat(cursos).hasSize(1);
        assertThat(cursos.get(0).getNome()).isEqualTo("Ciência da Computação");
    }

    @Test
    void deveRemoverCursoComSucesso() {
        when(cursoRepository.existsById(1L)).thenReturn(true);

        cursoService.removerCurso(1L);

        verify(cursoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveFalharAoRemoverCursoInexistente() {
        when(cursoRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> cursoService.removerCurso(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Curso não encontrado para exclusão");
    }
}
