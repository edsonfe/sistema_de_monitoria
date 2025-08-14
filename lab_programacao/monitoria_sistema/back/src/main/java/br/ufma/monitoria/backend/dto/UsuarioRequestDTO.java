package br.ufma.monitoria.backend.dto;

import java.time.LocalDate;

import br.ufma.monitoria.backend.model.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {
    private String nome;
    private String email;
    private String senha;
    private String celular;
    private LocalDate dataNascimento;
    private String matricula;
    private TipoUsuario tipoUsuario;
    private Long cursoId;  // Referência para o curso do usuário
}
