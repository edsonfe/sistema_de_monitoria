import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import SessaoInfo from '../components/Sessoes/SessaoInfo';
import LinkReuniao from '../components/Sessoes/LinkReuniao';
import BotoesAcoes from '../components/Sessoes/BotoesAcoes';
import ModalExclusao from '../components/Sessoes/ModalExclusao';
import '../styles/SessaoDetalhe.css';

export default function SessaoDetalhes() {
  const [modalVisivel, setModalVisivel] = useState(false);
  const [erro, setErro] = useState(null);

  const location = useLocation();
  const navigate = useNavigate();
  const { sessao } = location.state || {};

  if (!sessao) {
    navigate('/sessao-aluno', { replace: true });
    return null; // evita renderização com dados inválidos
  }

  async function excluirSessao(sessaoId) {
    try {
      const response = await fetch(`http://localhost:8080/api/sessoes/${sessaoId}`, {
        method: 'DELETE',
      });
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Erro ao excluir sessão');
      }
      setModalVisivel(false);
      navigate('/sessao-aluno');
    } catch (error) {
      setErro(error.message);
    }
  }

  const handleExcluirConfirmado = () => {
    excluirSessao(sessao.sessaoId);
  };

  const handleExcluir = () => setModalVisivel(true);
  const fecharModal = () => {
    setModalVisivel(false);
    setErro(null);
  };

  return (
    <div className="content sessao-detalhes">
      <div className="voltar-home" onClick={() => navigate(-1, { replace: true })} style={{ cursor: 'pointer' }}>
        <img src="https://img.icons8.com/ios-filled/24/03bcd3/left.png" alt="Voltar" />
        <span>Voltar</span>
      </div>

      <SessaoInfo
        titulo={sessao.titulo || sessao.disciplinaMonitoria}
        monitor={sessao.monitorNome}  // aqui passa o nome do monitor
        horarios={sessao.horarios || [sessao.horario]} // adapta caso horários seja um array ou string
      />
      <LinkReuniao link={sessao.linkSalaVirtual || sessao.link} />


      {erro && <p style={{ color: 'red' }}>Erro: {erro}</p>}

      <BotoesAcoes
        onMateriais={() => navigate('/materiais')}
        onChat={() => navigate('/chat')}
        onPrimario={() => navigate('/avaliacao', { state: { sessaoId: sessao.sessaoId } })}
        onExcluir={handleExcluir}
        tipo="aluno"
      />

      {modalVisivel && (
        <ModalExclusao
          onClose={fecharModal}
          onConfirmar={handleExcluirConfirmado}
        />
      )}
    </div>
  );
}
