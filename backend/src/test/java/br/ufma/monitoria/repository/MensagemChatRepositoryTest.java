package br.ufma.monitoria.repository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.ufma.monitoria.model.MensagemChat;
import br.ufma.monitoria.model.Monitoria;
import br.ufma.monitoria.model.SessaoMonitoria;
import br.ufma.monitoria.model.StatusSessao;
import br.ufma.monitoria.model.TipoUsuario;
import br.ufma.monitoria.model.Usuario;

@DataJpaTest
class MensagemChatRepositoryTest {

    @Autowired
    private MensagemChatRepository mensagemRepository;

    @Autowired
    private SessaoMonitoriaRepository sessaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Test
    void deveBuscarMensagensPorSessao() {
        // 1. Persistir o monitor
        Usuario monitor = Usuario.builder()
            .nome("Monitor João")
            .email("joao@ufma.br")
            .senha("123456")
            .tipo(TipoUsuario.MONITOR)
            .build();
        monitor = usuarioRepository.save(monitor);

        // 2. Persistir o aluno (autor da mensagem)
        Usuario aluno = Usuario.builder()
            .nome("Aluno Maria")
            .email("maria@ufma.br")
            .senha("654321")
            .tipo(TipoUsuario.ALUNO)
            .build();
        aluno = usuarioRepository.save(aluno);

        // 3. Persistir a monitoria
        Monitoria monitoria = Monitoria.builder()
            .codigo("MON202")
            .curso("Banco de Dados")
            .disciplina("Modelagem")
            .monitor(monitor)
            .build();
        monitoria = monitoriaRepository.save(monitoria);

        // 4. Persistir a sessão de monitoria
        SessaoMonitoria sessao = SessaoMonitoria.builder()
            .monitoria(monitoria)
            .aluno(aluno)
            .dataHora(LocalDateTime.now())
            .tema("Normalização")
            .status(StatusSessao.CONCLUIDA)
            .build();
        sessao = sessaoRepository.save(sessao);

        // 5. Persistir a mensagem
        MensagemChat mensagem = MensagemChat.builder()
            .conteudo("Essa parte foi bem complicada...")
            .dataHora(LocalDateTime.now())
            .autor(aluno) // referência correta ao autor
            .sessaoMonitoria(sessao)
            .build();
        mensagemRepository.save(mensagem);

        // 6. Verificar se a mensagem foi recuperada corretamente
        assertEquals(1, mensagemRepository.findBySessaoMonitoriaId(sessao.getId()).size());
    }
}
