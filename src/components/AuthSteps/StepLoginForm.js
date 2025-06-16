export default function StepLoginForm({ onEntrar }) {
  return (
    <div className="log-step active">
      <h2>Entrar</h2>
      <div className="form">
        <label htmlFor="email">E-mail</label>
        <input type="email" placeholder="Informe o e-mail cadastrado" />
      </div>
      <div className="form">
        <label htmlFor="senha">Senha</label>
        <input type="password" placeholder="Informe a senha cadastrada" />
      </div>
      <button className="btn-form" onClick={onEntrar}>
        Entrar
      </button>
    </div>
  );
}
