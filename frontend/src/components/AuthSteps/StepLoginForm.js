import { useState } from 'react';

export default function StepLoginForm({ onEntrar }) {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');

  const handleLogin = () => {
    fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, senha }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Falha no login');
        return res.json();
      })
      .then(data => {
        console.log('Usuário logado:', data);
        onEntrar(); // redireciona para home-aluno ou home-monitor
      })
      .catch(error => {
        alert('Erro ao fazer login: ' + error.message);
      });
  };

  return (
    <div className="log-step active">
      <h2>Entrar</h2>
      <div className="form">
        <label htmlFor="email">E-mail</label>
        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
      </div>
      <div className="form">
        <label htmlFor="senha">Senha</label>
        <input type="password" value={senha} onChange={(e) => setSenha(e.target.value)} />
      </div>
      <button className="btn-form" onClick={handleLogin}>Entrar</button>
    </div>
  );
}
