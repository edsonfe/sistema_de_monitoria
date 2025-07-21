package br.ufma.monitoria.service.contract;

import br.ufma.monitoria.model.Disciplina;
import java.util.List;

public interface DisciplinaServiceInterface {
    Disciplina salvar(Disciplina disciplina);
    Disciplina buscarPorId(Integer id);
    List<Disciplina> listarTodas();
}
