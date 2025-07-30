import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/AvaliacaoSessao.css';

export default function AvaliacaoSessao() {
  const [estrelas, setEstrelas] = useState(0);
  const [comentario, setComentario] = useState('');
  const navigate = useNavigate();

  const handleEstrelaClick = (value) => setEstrelas(value);

  const handleEnviar = () => {
    if (estrelas === 0) {
      alert('Por favor, selecione uma nota antes de enviar.');
      return;
    }

    // Aqui você pode enviar para API
    console.log({
      nota: estrelas,
      comentario
    });

    alert('Avaliação enviada com sucesso!');
    navigate('/sessao-aluno');
  };

  return (
    <div className="content avaliacao">
      <h2>Avaliar sessão</h2>
      <p>Como foi sua monitoria?</p>

      <div className="estrelas">
        {[1, 2, 3, 4, 5].map((value) => (
          <span
            key={value}
            className={`estrela ${value <= estrelas ? 'ativa' : ''}`}
            onClick={() => handleEstrelaClick(value)}
          >
            ★
          </span>
        ))}
      </div>

      <textarea
        placeholder="Comentário (opcional)"
        value={comentario}
        onChange={(e) => setComentario(e.target.value)}
      ></textarea>

      <button className="btn-enviar" onClick={handleEnviar}>
        Enviar
      </button>
    </div>
  );
}
