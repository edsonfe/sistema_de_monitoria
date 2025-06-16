import { useNavigate } from 'react-router-dom';

export default function CardsPainel() {
  const navigate = useNavigate();

  return (
    <div className="cards">
      <div className="card azul" onClick={() => navigate('/monitorias')}>
        <h3>Buscar</h3>
        <span>monitorias</span>
      </div>
      <div className="card" onClick={() => navigate('/sessoes')}>
        <h3>Minhas</h3>
        <span>sessões</span>
      </div>
    </div>
  );
}
