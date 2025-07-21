package br.ufma.monitoria.service.contract;

import br.ufma.monitoria.model.Curso;
import java.util.List;

public interface CursoServiceInterface {
    Curso salvar(Curso curso);
    Curso buscarPorId(Integer id);
    List<Curso> listarTodos();
}
