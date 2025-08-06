import { useNavigate } from 'react-router-dom';
import logo from '../../assets/logo1.ico';

export default function Sidebar() {
  const navigate = useNavigate();

  return (
    <div className="sidebar">
      <div>
        <div className="logo">
          <img src={logo} alt="logo" />
        </div>
        <div className="menu">
          <button onClick={() => navigate('/cadastrar')}>Nova Monitoria</button>
          <button onClick={() => navigate('/cadastradas')}>Sessões Agendadas</button>
        </div>
      </div>
      <div className="logout-button">
        <button onClick={() => navigate('/login')}>Sair</button>
      </div>
    </div>
  );
}
