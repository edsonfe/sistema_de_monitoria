import { useState } from 'react';

import Sidebar from '../components/Sidebar';
import Topbar from '../components/Topbar';
import CardsPainel from '../components/CardsPainel';
import '../styles/HomeAluno.css';

export default function HomeAluno() {
  const [usuario] = useState({
    nome: 'Edson',
    tipo: 'aluno'
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
