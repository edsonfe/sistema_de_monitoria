export default function Step2({ onNext, onBack }) {
  return (
    <div className="form-step active">
      <label>Curso</label>
      <select required>
        <option disabled selected>Selecione o curso</option>
        <option>Curso 1</option>
        <option>Curso 2</option>
      </select>

      <label>Matrícula</label>
      <input type="text" placeholder="Matrícula" required />

      <label>Cadeiras matriculado</label>
      <select multiple required size="3">
        <option disabled>Selecione as cadeiras (múltiplas)</option>
        <option>Cadeira 1</option>
        <option>Cadeira 2</option>
        <option>Cadeira 3</option>
        <option>Cadeira 4</option>
      </select>

      <div className="btns">
        <button type="button" className="btn" onClick={onBack}>Voltar</button>
        <button type="button" className="btn next" onClick={onNext}>Continuar</button>
      </div>
    </div>
  );
}
