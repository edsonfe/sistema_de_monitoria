package br.ufma.monitoria.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufma.monitoria.backend.dto.UsuarioRequestDTO;
import br.ufma.monitoria.backend.dto.UsuarioResponseDTO;
import br.ufma.monitoria.backend.model.Curso;
import br.ufma.monitoria.backend.model.Usuario;
import br.ufma.monitoria.backend.service.CursoService;
import br.ufma.monitoria.backend.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final CursoService cursoService;

    // ---------- LISTAR ----------
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            return ResponseEntity.ok(toResponseDTO(usuario));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ---------- CADASTRAR ----------
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody UsuarioRequestDTO dto,
            @RequestParam(required = false) String tokenMonitor) {
        try {
            Curso curso = cursoService.buscarPorId(dto.getCursoId());

            Usuario usuario = Usuario.builder()
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .senha(dto.getSenha())
                    .celular(dto.getCelular())
                    .dataNascimento(dto.getDataNascimento())
                    .matricula(dto.getMatricula())
                    .tipoUsuario(dto.getTipoUsuario())
                    .curso(curso)
                    .build();

            Usuario salvo = usuarioService.cadastrarUsuario(usuario, tokenMonitor);
            return ResponseEntity.ok(toResponseDTO(salvo));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Curso não encontrado");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ---------- ATUALIZAR ----------
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        try {
            Curso curso = cursoService.buscarPorId(dto.getCursoId());

            Usuario usuario = Usuario.builder()
                    .usuarioId(id)
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .senha(dto.getSenha())
                    .celular(dto.getCelular())
                    .dataNascimento(dto.getDataNascimento())
                    .matricula(dto.getMatricula())
                    .tipoUsuario(dto.getTipoUsuario())
                    .curso(curso)
                    .build();

            Usuario atualizado = usuarioService.atualizarUsuario(usuario);
            return ResponseEntity.ok(toResponseDTO(atualizado));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Curso ou usuário não encontrado");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ---------- REMOVER ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        try {
            usuarioService.removerUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ---------- LOGIN ----------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioRequestDTO dto) {
        try {
            Usuario usuario = usuarioService.loginComTipo(
                    dto.getEmail(),
                    dto.getSenha(),
                    dto.getTipoUsuario());
            return ResponseEntity.ok(toResponseDTO(usuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // ---------- VERIFICAÇÕES ----------
    @GetMapping("/exists/matricula/{matricula}")
    public ResponseEntity<Boolean> existsByMatricula(@PathVariable String matricula) {
        boolean exists = usuarioService.existsByMatricula(matricula);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = usuarioService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    // ---------- CONVERSÃO PARA DTO ----------
    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .usuarioId(usuario.getUsuarioId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .celular(usuario.getCelular())
                .dataNascimento(usuario.getDataNascimento())
                .matricula(usuario.getMatricula())
                .tipoUsuario(usuario.getTipoUsuario())
                .cursoId(usuario.getCurso().getCursoId())
                .cursoNome(usuario.getCurso().getNome())
                .build();
    }
}
