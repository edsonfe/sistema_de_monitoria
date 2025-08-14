// src/components/AuthSteps/StepLoginForm.js
import { useState } from 'react';

const ROLE_TO_TIPO_USUARIO = {
  'home-aluno': 'ALUNO',
  'home-monitor': 'MONITOR',
};

export default function StepLoginForm({ onEntrar, role }) {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = () => {
    if (!role) {
      alert('Selecione o tipo de usuário antes de entrar.');
      return;
    }
    if (!email || !senha) {
      alert('Preencha e-mail e senha.');
      return;
    }

    setLoading(true);

    fetch('http://localhost:8080/api/usuarios/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        email,
        senha,
        tipoUsuario: ROLE_TO_TIPO_USUARIO[role] || '',
      }),
    })
      .then(async (res) => {
        setLoading(false);
        if (!res.ok) {
          const errorText = await res.text();
          throw new Error(errorText || 'Falha no login');
        }
        return res.json();
      })
      .then((data) => {
        // Salva dados do usuário no localStorage
        localStorage.setItem('usuarioId', data.usuarioId);
        localStorage.setItem('tipoUsuario', data.tipoUsuario);
        localStorage.setItem('usuarioNome', data.nome);

        onEntrar();
      })
      .catch((error) => {
        alert('Erro ao fazer login: ' + error.message);
      });
  };

  return (
    <div className="log-step active">
      <h2>Entrar como {role === 'home-aluno' ? 'Aluno' : 'Monitor'}</h2>
      <div className="form">
        <label htmlFor="email">E-mail</label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          disabled={loading}
        />
      </div>
      <div className="form">
        <label htmlFor="senha">Senha</label>
        <input
          id="senha"
          type="password"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
          disabled={loading}
        />
      </div>
      <button className="btn-form" onClick={handleLogin} disabled={loading}>
        {loading ? 'Entrando...' : 'Entrar'}
      </button>
    </div>
  );
}
