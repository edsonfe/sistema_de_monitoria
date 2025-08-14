import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import SessaoInfo from '../components/Sessoes/SessaoInfo';
import LinkReuniao from '../components/Sessoes/LinkReuniao';
import BotoesAcoes from '../components/Sessoes/BotoesAcoes';
import ModalExclusao from '../components/Sessoes/ModalExclusao';

export default function SessaoDetalheMonitor() {
  const [modalVisivel, setModalVisivel] = useState(false);
  const [erro, setErro] = useState(null);

  const location = useLocation();
  const navigate = useNavigate();
  const { sessao } = location.state || {};

  useEffect(() => {
    if (!sessao) {
      navigate('/sessao-monitor', { replace: true });
    }
  }, [sessao, navigate]);

  if (!sessao) return null; // evita erro de renderização

  async function excluirSessao(sessaoId) {
    try {
      const response = await fetch(`http://localhost:8080/api/sessoes/${sessaoId}`, {
        method: 'DELETE',
      });
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Erro ao excluir sessão');
      }
      // exclusão ok, fecha modal e volta para lista
      setModalVisivel(false);
      navigate('/sessao-monitor');
    } catch (error) {
      setErro(error.message);
    }
  }

  const handleExcluirConfirmado = () => {
    excluirSessao(sessao.sessaoId);
  };

  return (
    <div className="content sessao-detalhes">
      <div
        className="voltar-home"
        onClick={() => navigate(-1, { replace: true })}
        style={{ cursor: 'pointer' }}
      >
        <img src="https://img.icons8.com/ios-filled/24/03bcd3/left.png" alt="Voltar" />
        <span>Voltar</span>
      </div>

      <div className="sessao-topo">
        <div className="info-principal">
          <SessaoInfo {...sessao} />
          <LinkReuniao link={sessao.link} />
        </div>

        <div className="resumo-lateral">
          <div className="estrela-media">
            <span className="estrela">★</span> {sessao.mediaAvaliacao ?? 4.8}
            <small>média</small>
          </div>

          <div className="comentarios">
            <div className="comentario-titulo">
              <img
                src="https://img.icons8.com/ios-filled/24/03bcd3/comments.png"
                alt="Comentários"/>
              <span>Comentários</span>
            </div>
            <ul>
              {(sessao.comentarios || []).map((c, i) => (
                <li key={i}>"{c}"</li>
              ))}
            </ul>
          </div>
        </div>
      </div>

      {erro && <p style={{ color: 'red' }}>Erro: {erro}</p>}

      <BotoesAcoes
        onMateriais={() =>
          navigate('/material-apoio', { state: { sessaoId: sessao.sessaoId } })
        }

        onChat={() => navigate(`/chat/${sessao.sessaoId}`, { state: { sessaoId: sessao.sessaoId } })}
        onPrimario={() =>
          navigate('/editar-monitoria', {
            state: {
              sessao: {
                ...sessao,
                titulo: sessao.titulo,
                curso: 'Ciência da Computação',
                codigo: 'COMP123',
                horario1: '09:00',
                horario2: '10:00',
                dia: '2025-06-28',
                link: sessao.link,
                materiais: 'Slides da aula e lista 1',
                observacoes: 'Usar material complementar para revisão.',
              },
            },
          })
        }
        onExcluir={() => setModalVisivel(true)}
        tipo="monitor"
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
