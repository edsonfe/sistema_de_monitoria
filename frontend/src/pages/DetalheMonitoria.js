import { useNavigate, useLocation, useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import '../styles/DetalheMonitoria.css';

export default function DetalheMonitoria() {
  const navigate = useNavigate();
  const location = useLocation();
  const { id } = useParams();

  const monitoria = location.state?.monitoria;

  const [solicitando, setSolicitando] = useState(false);
  const [mensagem, setMensagem] = useState('');

  useEffect(() => {
    if (!monitoria) {
      alert('Informações da monitoria não encontradas.');
      navigate('/home-aluno'); // ou para dashboard aluno
    }
  }, [monitoria, navigate]);

  const handleSolicitarSessao = async () => {
    setMensagem('');
    setSolicitando(true);

    try {
      // Exemplo: obter alunoId do contexto ou storage
      const alunoId = localStorage.getItem('usuarioId'); // ajustar conforme seu auth

      if (!alunoId) {
        alert('Usuário não autenticado.');
        setSolicitando(false);
        return;
      }

      // Criar objeto da solicitação
      const payload = {
        alunoId: Number(alunoId),
        monitoriaId: monitoria.monitoriaId,
        data: new Date().toISOString(), // aqui você pode solicitar a data escolhida pelo aluno
        status: 'AGUARDANDO_APROVACAO' // status inicial
      };

      const response = await fetch('http://localhost:8080/api/sessoes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const erroMsg = await response.text();
        throw new Error(erroMsg || 'Erro ao solicitar sessão.');
      }

      setMensagem('Solicitação enviada com sucesso! Aguarde aprovação do monitor.');
    } catch (error) {
      setMensagem(`Erro: ${error.message}`);
    } finally {
      setSolicitando(false);
    }
  };

  if (!monitoria) return null;

  return (
    <div className="content detalhe-monitoria">
      <div className="voltar-home" onClick={() => navigate(-1)} style={{ cursor: 'pointer' }}>
        <img
          src="https://img.icons8.com/ios-filled/24/03bcd3/left.png"
          alt="Voltar"
        />
        <span>Voltar</span>
      </div>

      <h2>{monitoria.disciplina}</h2>
      <p>
        <strong>Monitor:</strong> {monitoria.monitorNome}
      </p>

      <div className="horarios">
        <p>
          <strong>Horários disponíveis:</strong>
        </p>
        <ul>
          {/* Caso tenha lista de horários, use monitoria.diasDaSemana + monitoria.horario, 
              ajuste aqui conforme os dados retornados */}
          <li>{monitoria.diasDaSemana} • {monitoria.horario}</li>
        </ul>
      </div>

      <button
        className="btn-agendar"
        onClick={handleSolicitarSessao}
        disabled={solicitando}
      >
        {solicitando ? 'Solicitando...' : 'Solicitar Sessão'}
      </button>

      {mensagem && <p>{mensagem}</p>}
    </div>
  );
}
