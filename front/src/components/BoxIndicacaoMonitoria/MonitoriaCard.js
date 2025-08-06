export default function MonitoriaCard({ nome, disciplina, horarios, onAgendar }) {
  return (
    <div className="monitor-card">
      <div className="monitor-info">
        <strong>{nome}</strong>
        <p>{disciplina}</p>
        <small>{horarios}</small>
      </div>
      <button onClick={onAgendar}>Agendar</button>
    </div>
  );
}
