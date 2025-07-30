package br.ufma.monitoria.dto;

import java.time.LocalDate;
import java.util.UUID;

import br.ufma.monitoria.model.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsuarioDetalhadoDTO {
    private UUID id;
    private String nome;
    private String email;
    private String cpf;
    private String matricula;
    private String curso;
    private TipoUsuario tipo;
    private String codigoVerificacao;
    private String telefone;
    private LocalDate dataNascimento;
}
