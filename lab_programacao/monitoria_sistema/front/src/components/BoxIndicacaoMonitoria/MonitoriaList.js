import MonitoriaCard from './MonitoriaCard';

export default function MonitoriaList({ lista }) {
  return (
    <div className="monitoria-lista">
      {lista.length > 0 ? (
        lista.map((m, i) => (
          <MonitoriaCard
            key={i}
            nome={m.nome}
            disciplina={m.disciplina}
            horarios={m.horarios}
            onAgendar={() => alert(`Agendado com ${m.nome}`)}
          />
        ))
      ) : (
        <p className="nenhuma-monitoria">Nenhuma monitoria encontrada.</p>
      )}
    </div>
  );
}
