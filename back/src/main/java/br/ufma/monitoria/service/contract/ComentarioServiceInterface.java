package br.ufma.monitoria.service.contract;

import br.ufma.monitoria.model.Comentario;
import java.util.List;

public interface ComentarioServiceInterface {
    Comentario salvar(Comentario comentario);
    Comentario buscarPorId(Integer id);
    List<Comentario> listarTodos();
}
