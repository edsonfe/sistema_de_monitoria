package br.ufma.monitoria.dto;

import org.springframework.stereotype.Component;

import br.ufma.monitoria.model.Usuario;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.setMatricula(dto.getMatricula());
        usuario.setTipo(dto.getTipo());
        usuario.setTelefone(dto.getTelefone());
        usuario.setDataNascimento(dto.getDataNascimento());
        return usuario;
    }

    public Usuario toEntity(UsuarioCadastroDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.setMatricula(dto.getMatricula());
        usuario.setCurso(dto.getCurso());
        usuario.setTipo(dto.getTipo());
        usuario.setCodigoVerificacao(dto.getCodigoVerificacao());
        usuario.setSenha(dto.getSenha());
        usuario.setTelefone(dto.getTelefone());
        usuario.setDataNascimento(dto.getDataNascimento());
        return usuario;
    }

    public UsuarioDTO toDTO(Usuario entity) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail(entity.getEmail());
        dto.setCpf(entity.getCpf());
        dto.setMatricula(entity.getMatricula());
        dto.setTipo(entity.getTipo());
        dto.setTelefone(entity.getTelefone());
        dto.setDataNascimento(entity.getDataNascimento());
        return dto;
    }

    public UsuarioDetalhadoDTO toDetalhadoDTO(Usuario entity) {
        UsuarioDetalhadoDTO dto = new UsuarioDetalhadoDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setCpf(entity.getCpf());
        dto.setMatricula(entity.getMatricula());
        dto.setCurso(entity.getCurso());
        dto.setTipo(entity.getTipo());
        dto.setCodigoVerificacao(entity.getCodigoVerificacao());
        dto.setTelefone(entity.getTelefone());
        dto.setDataNascimento(entity.getDataNascimento());
        return dto;
    }
}
