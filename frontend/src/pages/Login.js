import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import ilustracaoAlunos from '../assets/img-login1.png';
import Logo from '../components/Shared/Logo';
import StepEscolhaUsuario from '../components/AuthSteps/StepEscolhaUsuario';
import StepLoginForm from '../components/AuthSteps/StepLoginForm';

import '../styles/Login.css';

export default function Login() {
  const [step, setStep] = useState(1);
  const [role, setRole] = useState('');
  const navigate = useNavigate();

  const handleEscolha = (roleSelecionado) => {
    setRole(roleSelecionado);
    setStep(2);
  };

  const handleEntrar = () => {
    if (role === 'home-aluno') {
      navigate('/home-aluno');
    } else if (role === 'home-monitor') {
      navigate('/home-monitor');
    } else {
      alert('Erro: Nenhuma opção selecionada.');
    }
  };

  return (
    <div className="container">
      <div className="esquerda">
        <Logo />
        {step === 1 ? (
          <StepEscolhaUsuario onEscolher={handleEscolha} />
        ) : (
          <StepLoginForm onEntrar={handleEntrar} />
        )}
      </div>
      <div className="direita">
        <img src={ilustracaoAlunos} alt="Ilustração de alunos" />
      </div>
    </div>
  );
}
