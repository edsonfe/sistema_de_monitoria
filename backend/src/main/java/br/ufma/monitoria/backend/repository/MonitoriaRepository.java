package br.ufma.monitoria.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ufma.monitoria.backend.model.Monitoria;

@Repository
public interface MonitoriaRepository extends JpaRepository<Monitoria, Long> {

    // Buscar todas as monitorias de um curso específico
    List<Monitoria> findByCurso_CursoId(Long cursoId);

    // Buscar todas as monitorias criadas por um monitor específico
    List<Monitoria> findByMonitor_UsuarioId(Long monitorId);

    // Buscar monitorias por disciplina (contendo parte do nome)
    List<Monitoria> findByDisciplinaContainingIgnoreCase(String disciplina);

    // Buscar monitorias por código da disciplina
    List<Monitoria> findByCodigoDisciplina(String codigoDisciplina);

    @Query("SELECT m FROM Monitoria m WHERE " +
            "(:disciplina IS NOT NULL AND LOWER(m.disciplina) LIKE LOWER(CONCAT('%', :disciplina, '%')) " +
            "OR :monitor IS NOT NULL AND LOWER(m.monitor.nome) LIKE LOWER(CONCAT('%', :monitor, '%')))")
    List<Monitoria> buscarPorDisciplinaEporMonitor(
            @Param("disciplina") String disciplina,
            @Param("monitor") String monitor);

}
