import excluir from "../../assets/icon-excluir.png"

export default function ModalExclusao({ onClose }) {
  return (
    <div className="modal-exclusao">
      <div className="modal-conteudo">
        <img
          src= { excluir } width={50}
          alt="Excluído"
        />
        <p>Sessão removida com sucesso</p>
        <button onClick={onClose}>OK</button>
      </div>
    </div>
  );
}
