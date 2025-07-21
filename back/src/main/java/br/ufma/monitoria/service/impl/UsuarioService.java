package br.ufma.monitoria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.service.contract.UsuarioServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;

@Service
public class UsuarioService implements UsuarioServiceInterface {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario salvar(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RegraNegocioRuntime("Email já cadastrado");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioRuntime("Usuário não encontrado"));
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}
