import '../A_Login/styles.css';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import logoImg from '../../assets/logo1.ico';
import ilustraImg from '../../assets/img-login1.png';

const LoginForm = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');

  const handleLogin = () => {
    // Aqui você pode futuramente adicionar autenticação real
    if (email && senha) {
      navigate('/home-aluno');
    } else {
      alert('Por favor, preencha todos os campos.');
    }
  };

  return (
    <div className="container">
      <div className="esquerda">
        <img src={logoImg} alt="Logo Mentoria UFMA" className="logo-fixa" width="100" />
        <h2>Entrar</h2>

        <section className="form">
          <label htmlFor="email">E-mail</label>
          <input
            id="email"
            type="email"
            placeholder="Informe o e-mail cadastrado"
            value={email}
            onChange={e => setEmail(e.target.value)}
          />
        </section>

        <section className="form">
          <label htmlFor="senha">Senha</label>
          <input
            id="senha"
            type="password"
            placeholder="Informe a senha cadastrada"
            value={senha}
            onChange={e => setSenha(e.target.value)}
          />
        </section>

        <button className="btn-form" onClick={handleLogin}>
          Entrar
        </button>
      </div>

      <div className="direita">
        <img src={ilustraImg} alt="Ilustração de alunos" />
      </div>
    </div>
  );
};

export default LoginForm;
