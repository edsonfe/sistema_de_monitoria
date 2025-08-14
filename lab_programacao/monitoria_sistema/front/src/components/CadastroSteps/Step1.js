export default function Step1({ onNext }) {
  return (
    <div className="form-step active">
      <label>Nome</label>
      <input type="text" placeholder="Nome completo" required />

      <label>Celular</label>
      <input type="text" placeholder="Celular (opcional)" />

      <label>E-mail</label>
      <input type="email" placeholder="E-mail institucional" required />

      <label>Data de nascimento</label>
      <input type="date" required />

      <div className="btns">
        <button type="button" className="btn cancelar" onClick={() => window.history.back()}>Cancelar</button>
        <button type="button" className="btn next" onClick={onNext}>Continuar</button>

      </div>
    </div>
  );
}
