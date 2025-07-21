package br.ufma.monitoria.service;

import br.ufma.monitoria.model.*;
import br.ufma.monitoria.repository.*;
import br.ufma.monitoria.service.contract.NotificacaoServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class NotificacaoServiceTest {

    @Autowired
    private NotificacaoServiceInterface notificacaoService;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Test
    void deveSalvarNotificacaoValida() {
        Curso curso = cursoRepository.save(Curso.builder().nome("Psicologia").build());
        Disciplina disciplina = disciplinaRepository.save(Disciplina.builder().nome("Psicologia Cognitiva").build());
        Usuario autor = usuarioRepository.save(Usuario.builder()
            .nome("João").email("joao@email.com").senha("joao123")
            .tipo(TipoUsuario.MONITOR).curso(curso).disciplina(disciplina).build());
        Usuario destinatario = usuarioRepository.save(Usuario.builder()
            .nome("Camila").email("camila@email.com").senha("camila123")
            .tipo(TipoUsuario.ALUNO).curso(curso).disciplina(disciplina).build());

        Notificacao notificacao = Notificacao.builder()
            .mensagem("Sessão de monitoria reagendada para amanhã às 10h.")
            .dataEnvio(LocalDateTime.now())
            .autor(autor)
            .destinatario(destinatario)
            .build();

        Notificacao salva = notificacaoService.salvar(notificacao);

        Assertions.assertNotNull(salva.getId());
        Assertions.assertEquals("Sessão de monitoria reagendada para amanhã às 10h.", salva.getMensagem());
        Assertions.assertFalse(salva.getLida());
        Assertions.assertEquals(destinatario.getId(), salva.getDestinatario().getId());
    }

    @Test
    void deveBuscarNotificacaoPorId() {
        Notificacao n = notificacaoRepository.save(Notificacao.builder()
            .mensagem("Lembrete: você tem uma sessão hoje às 15h.")
            .dataEnvio(LocalDateTime.now())
            .build());

        Notificacao buscada = notificacaoService.buscarPorId(n.getId());

        Assertions.assertEquals("Lembrete: você tem uma sessão hoje às 15h.", buscada.getMensagem());
        Assertions.assertFalse(buscada.getLida());
    }

    @Test
    void deveLancarErroAoBuscarNotificacaoInexistente() {
        RegraNegocioRuntime erro = Assertions.assertThrows(RegraNegocioRuntime.class, () -> {
            notificacaoService.buscarPorId(999);
        });

        Assertions.assertEquals("Notificação não encontrada", erro.getMessage());
    }
}
