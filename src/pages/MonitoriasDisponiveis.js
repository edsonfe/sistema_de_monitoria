import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import PageContainer from '../components/Shared/PageContainer';
import SearchBox from '../components/BoxIndicacaoMonitoria/SearchBox';
import MonitoriaList from '../components/BoxIndicacaoMonitoria/MonitoriaList';

import logo from '../assets/logo1.ico';
import '../styles/MonitoriasDisponiveis.css';

export default function MonitoriasDisponiveis() {
  const [busca, setBusca] = useState('');
  const navigate = useNavigate();

  const todasMonitorias = [
    { nome: 'Monitor 01', disciplina: 'Estrutura de Dados I', horarios: 'Seg: 10:00 - 11:00 | Qua: 10:00 - 12:00' },
    { nome: 'Monitor 02', disciplina: 'Linguagem de Programação', horarios: 'Ter: 10:00 - 11:00 | Qui: 10:00 - 12:00' },
    { nome: 'Monitor 03', disciplina: 'Redes de Computadores', horarios: 'Sex: 10:00 - 11:00 | Sáb: 10:00 - 12:00' },
  ];

  const filtradas = todasMonitorias.filter(m =>
    m.disciplina.toLowerCase().includes(busca.toLowerCase())
  );

  return (
    <PageContainer>
      <img src={logo} alt="Logo Mentoria UFMA" className="logo-fixa" />

      <h2 className="titulo">Monitorias Disponíveis</h2>

      <SearchBox value={busca} onChange={(e) => setBusca(e.target.value)} />

      <MonitoriaList lista={filtradas} />

      <button className="btn-avancar" onClick={() => navigate('/home-aluno')}>Avançar</button>
    </PageContainer>
  );
}
