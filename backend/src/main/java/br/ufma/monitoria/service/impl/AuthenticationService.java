package br.ufma.monitoria.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.ufma.monitoria.dto.AuthResponseDTO;
import br.ufma.monitoria.exceptions.CredenciaisInvalidasException;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.security.JwtService;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO autenticar(String email, String senha) {
        String emailNormalizado = email.trim().toLowerCase();
        log.debug("Tentando autenticar usuário com email: {}", emailNormalizado);

        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> {
                    log.warn("Email não encontrado: {}", emailNormalizado);
                    return new CredenciaisInvalidasException("Email ou senha inválidos");
                });

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            log.warn("Senha inválida para o email: {}", emailNormalizado);
            throw new CredenciaisInvalidasException("Email ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario);
        log.debug("Token gerado com sucesso para o usuário: {}", emailNormalizado);
        return new AuthResponseDTO("Bearer " + token);
    }

}
