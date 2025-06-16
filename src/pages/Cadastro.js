import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import Step1 from '../components/CadastroSteps/Step1';
import Step2 from '../components/CadastroSteps/Step2';
import Step3 from '../components/CadastroSteps/Step3';

import imagemCadastro from '../assets/img-cadastro1.png';
import '../styles/Cadastro.css';

export default function Cadastro() {
  const [step, setStep] = useState(1);
  const navigate = useNavigate();

  const goToStep = (s) => setStep(s);

  const finalizar = () => {
    navigate('/monitorias'); // Rota
  };

  return (
    <div className="container">
      <div className="esquerda">
        <form className="form">
          <div className="topo">
            <h2>Cadastro</h2>
          </div>

          <div className="steps">
            {[1, 2, 3].map((n) => (
              <div className="step" key={n}>
                <div className={`circle ${step === n ? 'active' : ''}`}>{n}</div>
                <span>{['Inform. pessoais', 'Preferências', 'Senha'][n - 1]}</span>
              </div>
            ))}
          </div>

          {step === 1 && <Step1 onNext={() => goToStep(2)} />}
          {step === 2 && <Step2 onNext={() => goToStep(3)} onBack={() => goToStep(1)} />}
          {step === 3 && <Step3 onBack={() => goToStep(2)} onFinish={finalizar} />}
        </form>
      </div>

      <div className="direita">
        <img src={imagemCadastro} alt="Estudante" />
      </div>
    </div>
  );
}
