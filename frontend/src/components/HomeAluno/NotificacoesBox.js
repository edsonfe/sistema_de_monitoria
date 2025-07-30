import { useState, useEffect } from 'react';

export default function NotificacoesBox() {
  const [aberto, setAberto] = useState(false);
  const [notificacoes, setNotificacoes] = useState([]);

  useEffect(() => {
    setTimeout(() => {
      setNotificacoes([
        { texto: 'Nova mensagem de chat', link: '/chat' },
        { texto: 'Novo material disponível', link: '/materiais' }
      ]);
    }, 500);
  }, []);

  return (
    <div className="icons" onClick={() => setAberto(!aberto)}>
      <img src="https://img.icons8.com/ios/50/000000/appointment-reminders--v1.png" alt="notificação" />
      {notificacoes.length > 0 && <span className="indicador-notificacao" />}
      {aberto && (
        <div className="notificacoes-box">
          <h4>Notificações</h4>
          <ul>
            {notificacoes.map((n, i) => (
              <li key={i}>
                <a href={n.link}>{n.texto}</a>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
