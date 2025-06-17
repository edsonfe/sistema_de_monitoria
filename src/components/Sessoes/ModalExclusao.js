export default function ModalExclusao({ onClose }) {
  return (
    <div className="modal-exclusao">
      <div className="modal-conteudo">
        <img
          src="https://img.icons8.com/ios-filled/50/e74c3c/filled-trash.png"
          alt="Excluído"
        />
        <p>Sessão removida com sucesso</p>
        <button onClick={onClose}>OK</button>
      </div>
    </div>
  );
}
