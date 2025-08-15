import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import SessaoInfo from '../components/Sessoes/SessaoInfo';
import LinkReuniao from '../components/Sessoes/LinkReuniao';
import BotoesAcoes from '../components/Sessoes/BotoesAcoes';
import ModalExclusao from '../components/Sessoes/ModalExclusao';
import '../styles/SessaoDetalhe.css';

export default function SessaoDetalheMonitor() {
  const [modalVisivel, setModalVisivel] = useState(false);
  const [erro, setErro] = useState(null);
  const [comentarios, setComentarios] = useState([]);
  const [mediaEstrelas, setMediaEstrelas] = useState(0);

  const location = useLocation();
  const navigate = useNavigate();
  const { sessao } = location.state || {};

  useEffect(() => {
    if (!sessao) {
      navigate('/sessao-monitor', { replace: true });
      return;
    }

    // Buscar comentários da sessão
    fetch(`http://localhost:8080/api/avaliacoes/sessao/${sessao.sessaoId}`)
      .then(res => res.json())
      .then(data => setComentarios(data.map(a => a.comentario)))
      .catch(err => setErro(err.message));

    // Buscar média de estrelas
    fetch(`http://localhost:8080/api/avaliacoes/media/sessao/${sessao.sessaoId}`)
      .then(res => res.json())
      .then(media => setMediaEstrelas(media))
      .catch(err => console.error('Erro ao calcular média:', err));
  }, [sessao, navigate]);

  if (!sessao) return null;

  const handleExcluirConfirmado = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/sessoes/${sessao.sessaoId}`, {
        method: 'DELETE',
      });
      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Erro ao excluir sessão');
      }
      setModalVisivel(false);
      navigate('/sessao-monitor');
    } catch (err) {
      setErro(err.message);
    }
  };

  return (
    <div className="content sessao-detalhes">
      {/* Voltar */}
      <div className="voltar-home" onClick={() => navigate(-1)} style={{ cursor: 'pointer' }}>
        <img src="https://img.icons8.com/ios-filled/24/03bcd3/left.png" alt="Voltar" />
        <span>Voltar</span>
      </div>

      <div className="sessao-topo">
        <div className="info-principal">
          <SessaoInfo
            titulo={sessao.titulo}
            curso={sessao.curso}
            codigo={sessao.codigo}
            monitor={sessao.monitorNome || sessao.monitor?.nome || '—'}
            horario1={sessao.horario1}
            horario2={sessao.horario2}
            dia={sessao.dia}
          />
          <LinkReuniao link={sessao.link} />
        </div>

        <div className="resumo-lateral">
          <div className="estrela-media">
            <span className="estrela">★</span> {mediaEstrelas.toFixed(1)}
            <small>média</small>
          </div>

          <div className="comentarios">
            <div className="comentario-titulo">
              <img src="https://img.icons8.com/ios-filled/24/03bcd3/comments.png" alt="Comentários" />
              <span>Comentários</span>
            </div>
            <ul>
              {comentarios.length > 0
                ? comentarios.map((c, i) => <li key={i}>"{c}"</li>)
                : <li>Sem comentários ainda</li>
              }
            </ul>
          </div>
        </div>
      </div>

      {erro && <p style={{ color: 'red' }}>Erro: {erro}</p>}

      <BotoesAcoes
        tipo="monitor"
        onMateriais={() => navigate('/material-apoio', { state: { sessaoId: sessao.sessaoId } })}
        onChat={() => navigate(`/chat/${sessao.sessaoId}`, { state: { sessaoId: sessao.sessaoId } })}
        onPrimario={() =>
          navigate(`/editar-monitoria/${sessao.monitoriaId}`, {
            state: { sessao },
          })
        }
        onExcluir={() => setModalVisivel(true)}
      />

      {modalVisivel && (
        <ModalExclusao
          onClose={() => setModalVisivel(false)}
          onConfirmar={handleExcluirConfirmado}
        />
      )}
    </div>
  );
}
