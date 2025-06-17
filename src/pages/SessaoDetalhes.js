import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import SessaoInfo from '../components/Sessoes/SessaoInfo';
import LinkReuniao from '../components/Sessoes/LinkReuniao';
import BotoesAcoes from '../components/Sessoes/BotoesAcoes';
import ModalExclusao from '../components/Sessoes/ModalExclusao';
import '../styles/SessaoDetalhes.css';

export default function SessaoDetalhes() {
  const [modalVisivel, setModalVisivel] = useState(false);

  const location = useLocation();
  const navigate = useNavigate();
  const { sessao } = location.state || {};

  if (!sessao) {
    navigate('/sessao-aluno');
    return null; // evita erro de renderização
  }

  const handleExcluir = () => setModalVisivel(true);
  const fecharModal = () => {
    setModalVisivel(false);
    navigate('/sessao-aluno');
  };

  return (
    <div className="content sessao-detalhes">
      <div className="voltar-home" onClick={() => navigate(-1, {replace: true} )}>
        <img src="https://img.icons8.com/ios-filled/24/03bcd3/left.png" alt="Voltar" />
        <span>Voltar</span>
      </div>

      <SessaoInfo {...sessao} />
      <LinkReuniao link={sessao.link} />
      <BotoesAcoes
        onMateriais={() => navigate('/materiais')}
        onChat={() => navigate('/chat')}
        onAvaliar={() => navigate('/avaliacao')}
        onExcluir={handleExcluir}
      />
      {modalVisivel && <ModalExclusao onClose={fecharModal} />}
    </div>
  );
}
