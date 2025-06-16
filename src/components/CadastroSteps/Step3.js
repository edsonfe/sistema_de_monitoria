export default function Step3({ onBack, onFinish }) {
  return (
    <div className="form-step active">
      <label>Senha</label>
      <input type="password" placeholder="Informe a senha" required />

      <label>Confirme senha</label>
      <input type="password" placeholder="Confirme a senha" required />

      <div className="btns">
        <button type="button" className="btn" onClick={onBack}>Voltar</button>
        <button type="button" className="btn next" onClick={onFinish}>Finalizar</button>
      </div>
    </div>
  );
}
