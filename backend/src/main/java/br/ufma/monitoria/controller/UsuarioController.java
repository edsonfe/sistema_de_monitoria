package br.ufma.monitoria.controller;

import java.util.List;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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


import br.ufma.monitoria.dto.UsuarioCadastroDTO;
import br.ufma.monitoria.dto.UsuarioDTO;
import br.ufma.monitoria.dto.UsuarioDetalhadoDTO;
import br.ufma.monitoria.dto.UsuarioMapper;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;
import br.ufma.monitoria.service.contract.UsuarioServiceInterface;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServiceInterface usuarioService;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@Valid @RequestBody UsuarioCadastroDTO dto) {
        Usuario entity = usuarioMapper.toEntity(dto);
        Usuario salvo = usuarioService.criarUsuario(entity);
        UsuarioDTO resposta = usuarioMapper.toDTO(salvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalhadoDTO> buscarPorId(@PathVariable UUID id) {
        return usuarioService.buscarPorId(id)
                .map(usuarioMapper::toDetalhadoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email)
                .map(usuarioMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UsuarioDTO> buscarPorCpf(@PathVariable String cpf) {
        return usuarioService.buscarPorCpf(cpf)
                .map(usuarioMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<UsuarioDTO> buscarPorMatricula(@PathVariable String matricula) {
        return usuarioService.buscarPorMatricula(matricula)
                .map(usuarioMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo")
    public ResponseEntity<List<UsuarioDTO>> buscarPorTipo(@RequestParam TipoUsuario tipo) {
        List<UsuarioDTO> dtos = usuarioService.listarPorTipo(tipo)
                .stream().map(usuarioMapper::toDTO).toList();
        return dtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(
            @PathVariable UUID id,
            @Valid @RequestBody UsuarioCadastroDTO dtoAtualizado) {

        // Verifica se o usuário existe antes de atualizar
        Optional<Usuario> existente = usuarioService.buscarPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario entity = usuarioMapper.toEntity(dtoAtualizado);
        entity.setId(id); // mantém o ID do usuário original

        Usuario atualizado = usuarioService.salvar(entity);
        UsuarioDTO resposta = usuarioMapper.toDTO(atualizado);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable UUID id) {
        Optional<Usuario> existente = usuarioService.buscarPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.excluirPorId(id); // você precisa ter esse método no serviço
        return ResponseEntity.noContent().build();
    }

}
