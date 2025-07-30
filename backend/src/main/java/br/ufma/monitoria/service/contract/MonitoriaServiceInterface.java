package br.ufma.monitoria.service.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.ufma.monitoria.dto.MonitoriaCadastroDTO;
import br.ufma.monitoria.model.Monitoria;

public interface MonitoriaServiceInterface {

    Monitoria criar(MonitoriaCadastroDTO dto);

    Optional<Monitoria> buscarPorId(UUID id);

    List<Monitoria> listarTodos();

    List<Monitoria> listarPorCurso(String curso);

    List<Monitoria> listarPorDisciplina(String disciplina);

    List<Monitoria> listarPorMonitor(UUID monitorId);

    void deletar(UUID id);
}
