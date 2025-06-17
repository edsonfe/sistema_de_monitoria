export default function BotoesAcoes({ onMateriais, onChat, onAvaliar, onExcluir }) {
  return (
    <div className="botoes-acoes">
      <button className="btn-material" onClick={onMateriais}>Materiais de apoio</button>
      <button className="btn-chat" onClick={onChat}>Chat</button>
      <button className="btn-avaliar" onClick={onAvaliar}>Avaliar</button>
      <button className="btn-excluir" onClick={onExcluir}>Excluir</button>
    </div>
  );
}
