import { useState } from 'react';

import Sidebar from '../components/HomeMonitor/Sidebar';
import Topbar from '../components/HomeMonitor/Topbar';
import CardsPainel from '../components/HomeMonitor/CardsPainel';
import '../styles/Home.css';

export default function HomeMonitor() {
  const [usuario] = useState({
    nome: 'Edson',
    tipo: 'monitor'
  });

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
