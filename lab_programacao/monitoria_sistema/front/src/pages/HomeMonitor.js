import { useState, useEffect } from 'react';

import Sidebar from '../components/HomeMonitor/Sidebar';
import Topbar from '../components/HomeMonitor/Topbar';
import CardsPainel from '../components/HomeMonitor/CardsPainel';
import '../styles/Home.css';

export default function HomeMonitor() {
  const [usuario, setUsuario] = useState({ nome: '', tipo: 'monitor' });

  useEffect(() => {
    const nomeCompleto = localStorage.getItem('usuarioNome') || '';
    const primeiroNome = nomeCompleto.split(' ')[0] || '';
    setUsuario({ nome: primeiroNome, tipo: 'monitor' }); // ou 'aluno' no outro componente
  }, []);

  return (
    <div className="home-container">
      <Sidebar />
      <div className="main">
        <Topbar nome={usuario.nome} tipo={usuario.tipo} />
        <div className="content">
          <h2>Olá, {usuario.nome}</h2>
          <p>Acesso rápido às funcionalidades</p>
          <CardsPainel />
        </div>
      </div>
    </div>
  );
}
