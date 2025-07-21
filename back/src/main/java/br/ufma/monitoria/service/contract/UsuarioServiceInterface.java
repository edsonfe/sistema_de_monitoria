package br.ufma.monitoria.service.contract;

import java.util.List;

import br.ufma.monitoria.model.Usuario;

public interface UsuarioServiceInterface {
    Usuario salvar(Usuario usuario);
    Usuario buscarPorId(Integer id);
    List<Usuario> listarTodos();
}
