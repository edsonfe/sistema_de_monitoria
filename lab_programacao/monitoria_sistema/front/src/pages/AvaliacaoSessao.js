import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../styles/AvaliacaoSessao.css';

export default function AvaliacaoSessao() {
  const [estrelas, setEstrelas] = useState(0);
  const [comentario, setComentario] = useState('');
  const [enviando, setEnviando] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();

  // Pega o ID da sessão passada pela navegação
  const sessaoId = location.state?.sessaoId;

  // Pega ID do aluno do localStorage
  const alunoId = localStorage.getItem('usuarioId');

  const handleEstrelaClick = (value) => setEstrelas(value);

  const handleEnviar = async () => {
    if (estrelas === 0) {
      alert('Por favor, selecione uma nota antes de enviar.');
      return;
    }

    if (!sessaoId || !alunoId) {
      alert('Sessão ou usuário inválido. Faça login novamente.');
      navigate('/login');
      return;
    }

    try {
      setEnviando(true);
      const response = await fetch('http://localhost:8080/api/avaliacoes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          sessaoId: Number(sessaoId),
          alunoId: Number(alunoId),
          estrelas,
          comentario,
        }),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Erro ao enviar avaliação');
      }

      alert('Avaliação enviada com sucesso!');
      
      // Se quiser limpar o formulário em vez de navegar:
      // setEstrelas(0);
      // setComentario('');
      // Ou navegue para a página desejada:
      navigate('/sessao-aluno');
    } catch (error) {
      alert('Erro: ' + error.message);
    } finally {
      setEnviando(false);
    }
  };

  return (
    <div className="content avaliacao">
      <h2>Avaliar sessão</h2>
      <p>Como foi sua monitoria?</p>

      <div className="estrelas" role="radiogroup" aria-label="Avaliação em estrelas">
        {[1, 2, 3, 4, 5].map((value) => (
          <span
            key={value}
            className={`estrela ${value <= estrelas ? 'ativa' : ''}`}
            onClick={() => handleEstrelaClick(value)}
            role="radio"
            aria-checked={value === estrelas}
            tabIndex={0}
            onKeyDown={(e) => {
              if (e.key === 'Enter' || e.key === ' ') {
                handleEstrelaClick(value);
              }
            }}
            aria-label={`${value} estrela${value > 1 ? 's' : ''}`}
          >
            ★
          </span>
        ))}
      </div>

      <textarea
        placeholder="Comentário (opcional, até 500 caracteres)"
        value={comentario}
        maxLength={500}
        onChange={(e) => setComentario(e.target.value)}
        aria-label="Comentário da avaliação"
      ></textarea>

      <button
        className="btn-enviar"
        onClick={handleEnviar}
        disabled={enviando}
        aria-busy={enviando}
      >
        {enviando ? 'Enviando...' : 'Enviar'}
      </button>
    </div>
  );
}
