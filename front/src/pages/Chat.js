import { useNavigate } from 'react-router-dom';
import { useState, useRef, useEffect } from 'react';
import '../styles/Chat.css';

export default function Chat() {
  const [mensagens, setMensagens] = useState([
    { texto: 'Como posso te ajudar?', autor: 'monitor' }
  ]);
  const [novaMensagem, setNovaMensagem] = useState('');
  const chatRef = useRef(null);
  const navigate = useNavigate();

  const enviarMensagem = () => {
    const texto = novaMensagem.trim();
    if (texto !== '') {
      setMensagens((prev) => [...prev, { texto, autor: 'aluno' }]);
      setNovaMensagem('');
    }
  };

  useEffect(() => {
    chatRef.current?.scrollTo({
      top: chatRef.current.scrollHeight,
      behavior: 'smooth'
    });
  }, [mensagens]);

  return (
    <div className="content chat">
      <div className="voltar-home" onClick={() => navigate(-1)}>
        <img
          src="https://img.icons8.com/ios-filled/24/03bcd3/left.png"
          alt="Voltar"
        />
        <span style={{ marginLeft: '3px' }}>Voltar</span>
      </div>

      <div className="chat-header">
        <img
          src="https://img.icons8.com/ios-filled/50/user-male-circle.png"
          alt="Perfil Monitor"
          className="avatar"
        />
        <div className="infos">
          <strong>User</strong>
          <span className="online"> ● Online</span>
        </div>
      </div>

      <div className="chat-mensagens" ref={chatRef}>
        {mensagens.map((m, i) => (
          <div key={i} className={`mensagem ${m.autor}`}>
            {m.texto}
          </div>
        ))}
      </div>

      <div className="chat-input">
        <input
          type="text"
          placeholder="Digite sua mensagem..."
          value={novaMensagem}
          onChange={(e) => setNovaMensagem(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && enviarMensagem()}
        />
        <button type='button' className='btn enviar' onClick={enviarMensagem}>Enviar</button>
      </div>
    </div>
  );
}
