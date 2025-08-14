import excluir from '../../assets/icon-excluir.png';

export default function ModalExclusao({ onClose, onConfirmar }) {
  return (
    <div className="modal-exclusao">
      <div className="modal-conteudo">
        <img src={excluir} width={50} alt="Excluir" />
        <p>Tem certeza que deseja excluir esta sessão?</p>
        <div>
          <button onClick={onConfirmar} className="btn-confirmar">Sim, excluir</button>
          <button onClick={onClose} className="btn-cancelar">Cancelar</button>
        </div>
      </div>
    </div>
  );
}
