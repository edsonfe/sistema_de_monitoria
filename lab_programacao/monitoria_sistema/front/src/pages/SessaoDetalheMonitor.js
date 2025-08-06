import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import SessaoInfo from '../components/Sessoes/SessaoInfo';
import LinkReuniao from '../components/Sessoes/LinkReuniao';
import BotoesAcoes from '../components/Sessoes/BotoesAcoes';
import ModalExclusao from '../components/Sessoes/ModalExclusao';

import '../styles/SessaoDetalhe.css';

import IconComentario from '../assets/icon-comentario.png';


export default function SessaoDetalhes() {
  const [modalVisivel, setModalVisivel] = useState(false);

  const location = useLocation();
  const navigate = useNavigate();
  const { sessao } = location.state || {};

  if (!sessao) {
    navigate('/sessao-monitor');
    return null; // evita erro de renderização
  }

  const handleExcluir = () => setModalVisivel(true);
  const fecharModal = () => {
    setModalVisivel(false);
    navigate('/sessao-monitor');
  };

  return (
    <div className="content sessao-detalhes">
      <div className="voltar-home" onClick={() => navigate(-1, {replace: true} )}>
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
            <span className="estrela">★</span> {sessao.mediaAvaliacao || 4.8}
            <small>média</small>
          </div>

          <div className="comentarios">
            <div className="comentario-titulo">
              <img
                src={IconComentario}
                alt="Ícone Comentário" width={18}
              />
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

      <BotoesAcoes
        onMateriais={() => navigate('/material-apoio')}
        onChat={() => navigate('/chat')}
        onPrimario={() =>
          navigate('/editar-monitoria', {
            state: {
              sessao: {
                titulo: sessao.titulo,
                curso: 'Ciência da Computação', // ajuste conforme origem real
                codigo: 'COMP123',              // idem
                horario1: '09:00',
                horario2: '10:00',
                dia: '2025-06-28',
                link: sessao.link,
                materiais: 'Slides da aula e lista 1',
                observacoes: 'Usar material complementar para revisão.'
              }
            }
          })
        }
        onExcluir={handleExcluir}
        tipo="monitor"
      />

      {modalVisivel && <ModalExclusao onClose={fecharModal} />}
    </div>
  );
}
