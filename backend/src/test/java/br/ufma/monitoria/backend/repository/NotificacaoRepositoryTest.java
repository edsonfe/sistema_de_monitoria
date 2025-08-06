package br.ufma.monitoria.backend.repository;

import br.ufma.monitoria.backend.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NotificacaoRepositoryTest {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void deveSalvarEBuscarNotificacoesPorUsuario() {
        // ---------- 1. Criar Curso ----------
        Curso curso = Curso.builder()
                .nome("Sistemas de Informação")
                .codigo("SI01")
                .build();
        cursoRepository.save(curso);

        // ---------- 2. Criar Usuário Destino ----------
        Usuario usuario = Usuario.builder()
                .nome("Usuário Notificação")
                .email("user.notif@teste.com")
                .senha("123456")
                .celular("11999999999")
                .dataNascimento(LocalDate.of(1998, 6, 20))
                .curso(curso)
                .matricula("20250008")
                .tipoUsuario(TipoUsuario.ALUNO)
                .build();
        usuarioRepository.save(usuario);

        // ---------- 3. Criar Notificação ----------
        Notificacao notificacao = Notificacao.builder()
                .usuarioDestino(usuario)
                .mensagem("Sua sessão de monitoria foi aprovada!")
                .dataCriacao(LocalDateTime.now())
                .lida(false)
                .build();
        notificacaoRepository.save(notificacao);

        // ---------- 4. Buscar notificações por usuário ----------
        List<Notificacao> notificacoes = notificacaoRepository.findByUsuarioDestino_UsuarioId(usuario.getUsuarioId());

        // ---------- 5. Validações ----------
        assertThat(notificacoes).hasSize(1);
        assertThat(notificacoes.get(0).getMensagem())
                .isEqualTo("Sua sessão de monitoria foi aprovada!");
        assertThat(notificacoes.get(0).getLida()).isFalse();
        assertThat(notificacoes.get(0).getUsuarioDestino().getNome())
                .isEqualTo("Usuário Notificação");
    }
}
