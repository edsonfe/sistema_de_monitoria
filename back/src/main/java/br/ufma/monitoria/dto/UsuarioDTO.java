package br.ufma.monitoria.dto;

import java.time.LocalDateTime;
import java.util.Set;

import br.ufma.monitoria.model.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String nome;

    @Email
    private String email;

    @Size(min = 6)
    private String senha;

    private String matricula;
    private LocalDateTime dataNascimento;

    private TipoUsuario tipo;

    private String codigoVerificacao;

    private Integer cursoId;

    private Integer disciplinaMonitoradaId; // para monitores

    private Set<Integer> disciplinasIds; // para alunos
    private Set<Integer> monitoriaIds;   // para alunos
}
