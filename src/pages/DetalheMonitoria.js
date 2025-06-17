import { useNavigate, useLocation, useParams } from 'react-router-dom';
import { useEffect } from 'react';
import '../styles/DetalheMonitoria.css';

export default function DetalheMonitoria() {
  const navigate = useNavigate();
  const location = useLocation();
  // eslint-disable-next-line no-unused-vars
  const { id } = useParams();

  const monitoria = location.state?.monitoria;

  useEffect(() => {
    if (!monitoria) {
      alert('Informações da monitoria não encontradas.');
      navigate('/home-aluno'); // ou redirect pro dashboard
    }
  }, [monitoria, navigate]);

  const handleAgendar = () => {
    alert('Sessão agendada com sucesso!');
    // Futuro: enviar para API ou navegar
  };

  if (!monitoria) return null;

  return (
    <div className="content detalhe-monitoria">
      <div className="voltar-home" onClick={() => navigate(-1)}>
        <img
          src="https://img.icons8.com/ios-filled/24/03bcd3/left.png"
          alt="Voltar"
        />
        <span>Voltar</span>
      </div>

      <h2>{monitoria.disciplina}</h2>
      <p>
        <strong>Monitor:</strong> {monitoria.monitor}
      </p>

      <div className="horarios">
        <p>
          <strong>Horários disponíveis:</strong>
        </p>
        <ul>
          {monitoria.horarios.map((h, i) => (
            <li key={i}>🗓 {h}</li>
          ))}
        </ul>
      </div>

      <button className="btn-agendar" onClick={handleAgendar}>
        Agendar monitoria
      </button>
    </div>
  );
}
