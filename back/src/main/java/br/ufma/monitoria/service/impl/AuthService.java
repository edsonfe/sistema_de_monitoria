package br.ufma.monitoria.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.service.contract.AuthServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;

@Service
public class AuthService implements AuthServiceInterface {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public boolean efetuarLogin(String email, String senha) {
        Optional<Usuario> usr = usuarioRepository.findByEmail(email);

        if (!usr.isPresent())
            throw new RegraNegocioRuntime("Erro de autenticação: Email inválido.");

        if (!usr.get().getSenha().equals(senha))
            throw new RegraNegocioRuntime("Erro de autenticação: Senha incorreta.");

        return true;
    }
}
