import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Sessao.css';

export default function SessaoMonitor() {
  const navigate = useNavigate();
  const monitorId = localStorage.getItem('usuarioId');

  const [sessoesMonitor, setSessoesMonitor] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState(null);
  const [atualizando, setAtualizando] = useState(false);

  function parseSessao(sessaoRaw) {
    const dt = new Date(sessaoRaw.data);
    return {
      sessaoId: sessaoRaw.sessaoId,
      data: dt,
      dataFormatada: dt.toLocaleDateString(),
      horaFormatada: dt.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      titulo: sessaoRaw.disciplinaMonitoria,
      link: sessaoRaw.linkSalaVirtual,
      monitorNome: sessaoRaw.monitorNome,
      status: sessaoRaw.status,
      alunoNome: sessaoRaw.alunoNome
    };
  }

  useEffect(() => {
    carregarSessoes();
  }, [monitorId]);

  async function carregarSessoes() {
    try {
      setLoading(true);
      const res = await fetch(`http://localhost:8080/api/sessoes/monitor/${monitorId}`);
      if (!res.ok) throw new Error('Erro ao carregar sessões');
      const dataRaw = await res.json();
      const data = dataRaw.map(parseSessao);
      data.sort((a, b) => a.data - b.data);
      setSessoesMonitor(data);
      setErro(null);
    } catch (e) {
      setErro(e.message);
    } finally {
      setLoading(false);
    }
  }

  async function atualizarStatus(sessaoId, novoStatus) {
    try {
      setAtualizando(true);
      const res = await fetch(`http://localhost:8080/api/sessoes/${sessaoId}/status?status=${novoStatus}`, {
        method: 'PUT',
      });
      if (!res.ok) {
        const errData = await res.json();
        throw new Error(errData.message || 'Erro ao atualizar status');
      }
      // Atualizar estado local para refletir mudança
      setSessoesMonitor(prev =>
        prev.map(sessao =>
          sessao.sessaoId === sessaoId ? { ...sessao, status: novoStatus } : sessao
        )
      );
    } catch (e) {
      alert(`Erro: ${e.message}`);
    } finally {
      setAtualizando(false);
    }
  }

  if (loading) return <p>Carregando sessões...</p>;
  if (erro) return <p style={{ color: 'red' }}>Erro: {erro}</p>;
  if (sessoesMonitor.length === 0) return <p>Nenhuma sessão agendada.</p>;

  return (
    <div className="content">
      <div
        className="voltar-home"
        onClick={() => navigate('/home-monitor')}
        style={{ cursor: 'pointer' }}
      >
        <img
          src="https://img.icons8.com/ios-filled/24/03bcd3/left.png"
          alt="Voltar"
        />
        <span>Voltar</span>
      </div>

      <h2>Minhas sessões agendadas</h2>

      <div className="lista-sessoes">
        {sessoesMonitor.map(sessao => (
          <div
            key={sessao.sessaoId}
            className={`card-sessao status-${sessao.status.toLowerCase()}`}
            style={{ cursor: atualizando ? 'default' : 'pointer' }}
            onClick={() =>
              !atualizando &&
              navigate('/sessao-detalhe-monitor', {
                state: { sessao }
              })
            }
          >
            <h3>{sessao.titulo}</h3>
            <p>
              <strong>Data:</strong> {sessao.dataFormatada} <br />
              <strong>Hora:</strong> {sessao.horaFormatada}
            </p>
            <p>
              <strong>Status:</strong> {sessao.status} <br />
              <strong>Aluno:</strong> {sessao.alunoNome}
            </p>

            {sessao.status === 'AGUARDANDO_APROVACAO' && (
              <div className="acoes-status">
                <button
                  className="btn-deferir"
                  onClick={e => {
                    e.stopPropagation();
                    atualizarStatus(sessao.sessaoId, 'DEFERIDA');
                  }}
                  disabled={atualizando}
                >
                  Deferir
                </button>
                <button
                  className="btn-recusar"
                  onClick={e => {
                    e.stopPropagation();
                    atualizarStatus(sessao.sessaoId, 'RECUSADA');
                  }}
                  disabled={atualizando}
                >
                  Recusar
                </button>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
