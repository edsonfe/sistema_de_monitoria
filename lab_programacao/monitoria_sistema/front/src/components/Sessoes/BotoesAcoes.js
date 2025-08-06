export default function BotoesAcoes({ onMateriais, onChat, onPrimario, onExcluir, tipo }) {
  const isMonitor = tipo === 'monitor';

  return (
    <div className="botoes-acoes">
      <button className="btn-material" onClick={onMateriais}>
        {isMonitor ? 'Incluir material' : 'Materiais de apoio'}
      </button>
      <button className="btn-chat" onClick={onChat}>Chat</button>
      <button className="btn-avaliar" onClick={onPrimario}>
        {isMonitor ? 'Editar' : 'Avaliar'}
      </button>
      <button className="btn-excluir" onClick={onExcluir}>Excluir</button>
    </div>
  );
}
