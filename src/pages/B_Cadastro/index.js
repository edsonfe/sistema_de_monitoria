import './styles.css';
import cadastroImg from '../../assets/img-cadastro1.png';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Cadastro = () => {
  const [step, setStep] = useState(1);
  const navigate = useNavigate();

  const nextStep = (targetStep) => {
    setStep(targetStep);
  };

  const finalizar = () => {
    navigate('/agendamento'); // Criaremos essa rota depois, se quiser
  };

  return (
    <div className="container">
      <div className="esquerda">
        <form className="form" id="cadform">
          <div className="topo">
            <h2>Cadastro</h2>
          </div>

          <div className="steps">
            {[1, 2, 3].map((num) => (
              <div className="step" key={num}>
                <div className={`circle ${step === num ? 'active' : ''}`} id={`circle${num}`}>
                  {num}
                </div>
                <span>{['Inform. pessoais', 'Preferências', 'Senha'][num - 1]}</span>
              </div>
            ))}
          </div>

          {/* Etapa 1 */}
          {step === 1 && (
            <div className="form-step active" id="step1">
              <label>Nome</label>
              <input type="text" placeholder="Nome completo" required />
              <label>Celular</label>
              <input type="text" placeholder="Celular (opcional)" />
              <label>E-mail</label>
              <input type="email" placeholder="E-mail institucional" required />
              <label>Data de nascimento</label>
              <input type="date" required />
              <div className="btns">
                <button type="button" className="btn next" onClick={() => nextStep(2)}>
                  Continuar
                </button>
                <button type="button" className="btn-cancelar" onClick={() => navigate('/')}>
                  Cancelar
                </button>
              </div>
            </div>
          )}

          {/* Etapa 2 */}
          {step === 2 && (
            <div className="form-step active" id="step2">
              <label>Curso</label>
              <select required>
                <option disabled selected>
                  Selecione o curso
                </option>
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
                <button type="button" className="btn" onClick={() => nextStep(1)}>
                  Voltar
                </button>
                <button type="button" className="btn next" onClick={() => nextStep(3)}>
                  Continuar
                </button>
              </div>
            </div>
          )}

          {/* Etapa 3 */}
          {step === 3 && (
            <div className="form-step active" id="step3">
              <label>Senha</label>
              <input type="password" placeholder="Informe a senha" required />
              <label>Confirme senha</label>
              <input type="password" placeholder="Confirme a senha" required />
              <div className="btns">
                <button type="button" className="btn" onClick={() => nextStep(2)}>
                  Voltar
                </button>
                <button type="button" className="btn next" onClick={finalizar}>
                  Finalizar
                </button>
              </div>
            </div>
          )}
        </form>
      </div>

      <div className="direita">
        <img src={cadastroImg} alt="Estudante" />
      </div>
    </div>
  );
};

export default Cadastro;
