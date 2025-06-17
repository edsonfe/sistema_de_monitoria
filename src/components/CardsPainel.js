import { useNavigate } from 'react-router-dom';

export default function CardsPainel() {
  const navigate = useNavigate();

  return (
    <div className="cards">
      <div className="card" onClick={() => navigate('/buscar-monitoria')}>
        <h3>Buscar</h3>
        <span>monitorias</span>
      </div>
      <div className="card" onClick={() => navigate('/sessao-aluno')}>
        <h3>Minhas</h3>
        <span>sessões</span>
      </div>
    </div>
  );
}
