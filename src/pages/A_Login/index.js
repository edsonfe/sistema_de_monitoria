import { useNavigate } from 'react-router-dom';
import logoImg from '../../assets/logo1.ico';
import ilustraImg from '../../assets/img-login1.png';
import './styles.css';

const Login = () => {
  const navigate = useNavigate();

  return (
    <div className="login-wrapper">
      <div className="login-container">
        <div className="esquerda">
          <img src={logoImg} alt="Logo Mentoria UFMA" className="logo-fixa" />
          <div className="opcoes">
            <h2>Área</h2>
            <button className="btn" onClick={() => navigate('/login')}>Entrar como Aluno</button>
            <button className="btn" onClick={() => navigate('/login')}>Entrar como Mentor</button>
            <div className="linha-com-texto"><span>Primeiro acesso?</span></div>
            <button className="btn2" onClick={() => navigate('/cadastro')}>Cadastrar-se</button>
          </div>
        </div>
        <div className="direita">
          <img src={ilustraImg} alt="Ilustração de alunos" />
        </div>
      </div>
    </div>
  );
};

export default Login;
