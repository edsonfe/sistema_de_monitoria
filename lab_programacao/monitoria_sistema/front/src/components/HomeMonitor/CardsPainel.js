import { useNavigate } from 'react-router-dom';

export default function CardsPainel() {
  const navigate = useNavigate();

  return (
    <div className="cards">
      <div className="card" onClick={() => navigate('/cadastrar')}>
        <h3>Nova</h3>
        <span>monitoria</span>
      </div>
      <div className="card" onClick={() => navigate('/cadastradas')}>
        <h3>Sessões</h3>
        <span>agendadas</span>
      </div>
    </div>
  );
}
