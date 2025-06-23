import NotificacoesBox from './NotificacoesBox';
import { useNavigate } from 'react-router-dom';

export default function Topbar({ nome, tipo }) {
  const navigate = useNavigate();

  return (
    <div className="topbar">
      <NotificacoesBox />
      <div className="user perfil-link" onClick={() => navigate('/perfil')}>
        <img src="https://img.icons8.com/ios-filled/50/user-male-circle.png" alt="perfil" width="30" />
        <div>
          <span>{nome}</span>
          <strong>{tipo.charAt(0).toUpperCase() + tipo.slice(1)}</strong>
        </div>
      </div>
    </div>
  );
}
