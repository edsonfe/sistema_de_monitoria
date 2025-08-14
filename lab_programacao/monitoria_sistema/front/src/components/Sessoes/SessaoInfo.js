export default function SessaoInfo({ titulo, monitor, horarios }) {
  return (
    <>
      <h2 className="titulo-monitoria">{titulo}</h2>
      <p className="nome-monitor">
        <strong>Monitor:</strong> {monitor}
      </p>
      <ul className="horarios">
        {(horarios || []).map((horario, index) => (
          <li key={index}>{horario}</li>
        ))}
      </ul>
    </>
  );
}
