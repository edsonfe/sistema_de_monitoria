package br.ufma.monitoria.service.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;

public interface UsuarioServiceInterface {
    Usuario criarUsuario(Usuario usuario);

    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorMatricula(String matricula);

    Optional<Usuario> buscarPorCpf(String cpf);

    Optional<Usuario> buscarPorId(UUID id);

    List<Usuario> listarPorTipo(TipoUsuario tipo);

    Usuario salvar(Usuario usuario);

    void excluirPorId(UUID id);

}