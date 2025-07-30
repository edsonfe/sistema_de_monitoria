package br.ufma.monitoria.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.repository.UsuarioRepository;
import br.ufma.monitoria.service.contract.UsuarioServiceInterface;
import br.ufma.monitoria.service.exceptions.BusinessException;

@Service
public class UsuarioService implements UsuarioServiceInterface {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario criarUsuario(Usuario usuario) {
        validarUsuario(usuario);

        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        validarUsuario(usuario); // <- adiciona essa linha
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Optional<Usuario> buscarPorMatricula(String matricula) {
        return usuarioRepository.findByMatricula(matricula);
    }

    @Override
    public Optional<Usuario> buscarPorCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf);
    }

    @Override
    public List<Usuario> listarPorTipo(TipoUsuario tipo) {
        return usuarioRepository.findByTipo(tipo);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getTipo() == TipoUsuario.MONITOR &&
                (usuario.getCodigoVerificacao() == null || usuario.getCodigoVerificacao().isBlank())) {
            throw new BusinessException("Monitores precisam de código de verificação.");
        }

        if (usuario.getDataNascimento() != null && usuario.getDataNascimento().isAfter(LocalDate.now())) {
            throw new BusinessException("Data de nascimento inválida.");
        }

        if (usuario.getTelefone() != null && !usuario.getTelefone().matches("\\d{10,11}")) {
            throw new BusinessException("Formato de telefone inválido.");
        }
    }

    @Override
    public void excluirPorId(UUID id) {
        usuarioRepository.deleteById(id);
    }

}
