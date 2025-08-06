package br.ufma.monitoria.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufma.monitoria.backend.model.TipoUsuario;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;

  private static final String TOKEN_MONITOR = "MONITOR123";

  @Transactional
  public Usuario cadastrarUsuario(Usuario usuario, String tokenMonitor) {
    if (usuarioRepository.existsByEmail(usuario.getEmail())) {
      throw new IllegalArgumentException("E-mail já cadastrado.");
    }
    if (usuarioRepository.existsByMatricula(usuario.getMatricula())) {
      throw new IllegalArgumentException("Matrícula já cadastrada.");
    }
    if (usuario.getTipoUsuario() == TipoUsuario.MONITOR) {
      if (!TOKEN_MONITOR.equals(tokenMonitor)) {
        throw new IllegalArgumentException("Token de monitor inválido.");
      }
    }
    return usuarioRepository.save(usuario);
  }

  /*
   * public Usuario login(String email, String senha) {
   * return usuarioRepository.findByEmail(email)
   * .filter(u -> u.getSenha().equals(senha))
   * .orElseThrow(() -> new
   * IllegalArgumentException("E-mail ou senha inválidos"));
   * }
   */

  public Usuario loginComTipo(String email, String senha, TipoUsuario tipoUsuario) {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos"));

    if (!usuario.getSenha().equals(senha)) {
      throw new IllegalArgumentException("E-mail ou senha inválidos");
    }

    // Se o tipo selecionado no front não corresponder ao tipo do usuário cadastrado
    if (!usuario.getTipoUsuario().equals(tipoUsuario)) {
      throw new IllegalArgumentException("Tipo de usuário incorreto");
    }

    // Nenhuma validação extra para token no login
    return usuario;
  }



  

  public Usuario buscarPorId(Long id) {
    return usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
  }

  public List<Usuario> listarTodos() {
    return usuarioRepository.findAll();
  }

  @Transactional
  public Usuario atualizarUsuario(Usuario usuario) {
    Usuario existente = buscarPorId(usuario.getUsuarioId());
    // Atualiza campos necessários, por exemplo:
    existente.setNome(usuario.getNome());
    existente.setEmail(usuario.getEmail());
    existente.setSenha(usuario.getSenha());
    existente.setCelular(usuario.getCelular());
    existente.setDataNascimento(usuario.getDataNascimento());
    existente.setMatricula(usuario.getMatricula());
    existente.setTipoUsuario(usuario.getTipoUsuario());
    existente.setCurso(usuario.getCurso());
    return usuarioRepository.save(existente);
  }

  @Transactional
  public void removerUsuario(Long id) {
    if (!usuarioRepository.existsById(id)) {
      throw new EntityNotFoundException("Usuário não encontrado para exclusão");
    }
    usuarioRepository.deleteById(id);
  }

  public boolean existsByMatricula(String matricula) {
    return usuarioRepository.existsByMatricula(matricula);
  }

  public boolean existsByEmail(String email) {
    return usuarioRepository.existsByEmail(email);
  }
}
