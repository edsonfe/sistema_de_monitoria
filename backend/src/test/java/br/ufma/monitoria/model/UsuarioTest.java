package br.ufma.monitoria.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void deveCriarUsuarioComCamposObrigatorios() {
        Usuario usuario = Usuario.builder()
            .id(UUID.randomUUID())
            .nome("Carlos Silva")
            .dataNascimento(LocalDate.of(2001, 10, 5))
            .email("carlos.silva@exemplo.com")
            .senha("senhaSegura123")
            .tipo(TipoUsuario.ALUNO)
            .build();

        assertNotNull(usuario.getId());
        assertEquals("Carlos Silva", usuario.getNome());
        assertEquals(LocalDate.of(2001, 10, 5), usuario.getDataNascimento());
        assertEquals("carlos.silva@exemplo.com", usuario.getEmail());
        assertEquals("senhaSegura123", usuario.getSenha());
        assertEquals(TipoUsuario.ALUNO, usuario.getTipo());
    }

    @Test
    void podeCriarUsuarioComCamposOpcionais() {
        Usuario usuario = Usuario.builder()
            .nome("Mariana")
            .email("mariana@teste.com")
            .senha("1234")
            .tipo(TipoUsuario.MONITOR)
            .telefone("98 99100-1234")
            .cpf("123.456.789-10")
            .matricula("2022101001")
            .curso("Direito")
            .codigoVerificacao("ABC123")
            .build();

        assertEquals("Mariana", usuario.getNome());
        assertEquals("Direito", usuario.getCurso());
        assertEquals("ABC123", usuario.getCodigoVerificacao());
        assertEquals("123.456.789-10", usuario.getCpf());
    }

    @Test
    void listasRelacionadasPodemSerNulasOuVazias() {
        Usuario usuario = Usuario.builder()
            .email("teste@teste.com")
            .senha("teste")
            .tipo(TipoUsuario.ADMIN)
            .build();

        assertTrue(usuario.getSessoes() == null || usuario.getSessoes().isEmpty());
        assertTrue(usuario.getMensagens() == null || usuario.getMensagens().isEmpty());
        assertTrue(usuario.getMonitorias() == null || usuario.getMonitorias().isEmpty());
    }
}
