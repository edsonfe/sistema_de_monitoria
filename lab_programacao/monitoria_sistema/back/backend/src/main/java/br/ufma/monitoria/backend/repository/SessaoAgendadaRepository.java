package br.ufma.monitoria.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.backend.model.SessaoAgendada;
import br.ufma.monitoria.backend.model.StatusSessao;

@Repository
public interface SessaoAgendadaRepository extends JpaRepository<SessaoAgendada, Long> {

    // Listar todas as sessões de um aluno específico
    List<SessaoAgendada> findByAluno_UsuarioId(Long alunoId);

    // Listar todas as sessões de monitorias criadas por um monitor específico
    List<SessaoAgendada> findByMonitoria_Monitor_UsuarioId(Long monitorId);

    // Buscar sessões por status (aguardando aprovação, deferida, recusada)
    List<SessaoAgendada> findByStatus(StatusSessao status);

    // Buscar sessões futuras de um aluno
    List<SessaoAgendada> findByAluno_UsuarioIdAndDataAfter(Long alunoId, LocalDateTime data);

    // Buscar sessões futuras de um monitor
    List<SessaoAgendada> findByMonitoria_Monitor_UsuarioIdAndDataAfter(Long monitorId, LocalDateTime data);

    List<SessaoAgendada> findByAluno_UsuarioIdAndStatus(Long alunoId, StatusSessao status);

}