import { useNavigate, useParams, useLocation } from 'react-router-dom';
import { useState, useRef, useEffect } from 'react';
import '../styles/Chat.css';

export default function Chat() {
  const [mensagens, setMensagens] = useState([]);
  const [novaMensagem, setNovaMensagem] = useState('');
  const chatRef = useRef(null);
  const navigate = useNavigate();
  const { sessaoId: sessaoIdParam } = useParams();
  const location = useLocation();

  // tenta pegar da URL; se não tiver, pega do state
  const sessaoIdFromState = location.state?.sessaoId;
  const sessaoIdNum = Number(sessaoIdParam ?? sessaoIdFromState);

  const autorIdStr = localStorage.getItem('usuarioId');
  const autorId = autorIdStr ? Number(autorIdStr) : null;

  // Redireciona se faltar contexto
  useEffect(() => {
    if (!Number.isFinite(sessaoIdNum) || !autorId) {
      alert('Sessão ou usuário não identificados. Retornando...');
      navigate(-1);
    }
  }, [sessaoIdNum, autorId, navigate]);

  const carregarMensagens = async () => {
    if (!Number.isFinite(sessaoIdNum)) return;
    try {
      const res = await fetch(`http://localhost:8080/api/mensagens/sessao/${sessaoIdNum}`);
      if (!res.ok) throw new Error('Erro ao carregar mensagens');
      const data = await res.json();
      setMensagens(data); // mantém o shape do back: {mensagemId, conteudo, autorId, autorNome, dataHora, ...}
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    carregarMensagens();
    const id = setInterval(carregarMensagens, 3000);
    return () => clearInterval(id);
  }, [sessaoIdNum]);

  // scroll automático
  useEffect(() => {
    chatRef.current?.scrollTo({ top: chatRef.current.scrollHeight, behavior: 'smooth' });
  }, [mensagens]);

  const enviarMensagem = async () => {
    const texto = novaMensagem.trim();
    if (!texto) return;

    if (!Number.isFinite(sessaoIdNum) || !Number.isFinite(autorId)) {
      alert('Sessão ou usuário inválidos.');
      return;
    }

    const mensagemDTO = {
      conteudo: texto,
      autorId: autorId,
      sessaoId: sessaoIdNum,
    };

    try {
      const res = await fetch('http://localhost:8080/api/mensagens', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(mensagemDTO),
      });

      if (!res.ok) {
        const err = await res.text();
        throw new Error(err || 'Erro ao enviar mensagem.');
      }

      const novaMsg = await res.json(); // já vem no formato do back
      setMensagens(prev => [...prev, novaMsg]);
      setNovaMensagem('');
    } catch (error) {
      console.error('Erro na requisição:', error);
      alert('Erro na comunicação com o servidor.');
    }
  };

  return (
    <div className="content chat">
      <div className="voltar-home" onClick={() => navigate(-1)} style={{ cursor: 'pointer' }}>
        <img src="https://img.icons8.com/ios-filled/24/03bcd3/left.png" alt="Voltar" />
        <span style={{ marginLeft: 3 }}>Voltar</span>
      </div>

      <div className="chat-header">
        <img
          src="https://img.icons8.com/ios-filled/50/user-male-circle.png"
          alt="Perfil"
          className="avatar"
        />
        <div className="infos">
          <strong>Chat da Sessão #{Number.isFinite(sessaoIdNum) ? sessaoIdNum : '-'}</strong>
        </div>
      </div>

      <div className="chat-mensagens" ref={chatRef}>
        {mensagens.map(m => (
          <div
            key={m.mensagemId}
            className={`mensagem ${m.autorId === autorId ? 'me' : 'other'}`}
            title={m.dataHora}
          >
            <span className="autor">{m.autorNome}:</span> {m.conteudo}
          </div>
        ))}
      </div>

      <div className="chat-input">
        <input
          type="text"
          placeholder="Digite sua mensagem..."
          value={novaMensagem}
          onChange={e => setNovaMensagem(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && enviarMensagem()}
        />
        <button type="button" className="btn enviar" onClick={enviarMensagem}>
          <img src="https://img.icons8.com/ios-glyphs/24/ffffff/filled-sent.png" alt="Enviar" />
        </button>
      </div>
    </div>
      );
}
