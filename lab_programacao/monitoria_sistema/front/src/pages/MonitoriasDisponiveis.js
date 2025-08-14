import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import PageContainer from '../components/Shared/PageContainer';
import SearchBox from '../components/BoxIndicacaoMonitoria/SearchBox';
import MonitoriaList from '../components/BoxIndicacaoMonitoria/MonitoriaList';

import logo from '../assets/logo1.ico';
import '../styles/MonitoriasDisponiveis.css';

export default function MonitoriasDisponiveis() {
  const [busca, setBusca] = useState('');
  const [monitorias, setMonitorias] = useState([]);
  const navigate = useNavigate();
  const location = useLocation();
  const usuario = location.state?.usuario;

  useEffect(() => {
    fetch('http://localhost:8080/api/monitorias')
      .then(res => res.json())
      .then(data => setMonitorias(data))
      .catch(err => console.error(err));
  }, []);

  const filtradas = monitorias.filter(m =>
    m.disciplina.toLowerCase().includes(busca.toLowerCase())
  );

  return (
    <PageContainer>
      <img src={logo} alt="Logo Mentoria UFMA" className="logo-fixa" />
      <h2 className="titulo">Monitorias Disponíveis</h2>

      <SearchBox value={busca} onChange={(e) => setBusca(e.target.value)} />
      <MonitoriaList lista={filtradas} />

      <button className="btn-avancar" onClick={() => navigate('/home-aluno', { state: { usuario } })}>
        Avançar
      </button>
    </PageContainer>
  );
}
