import { useState, useEffect } from 'react';

export default function NotificacoesBox() {
  const [aberto, setAberto] = useState(false);
  const [notificacoes, setNotificacoes] = useState([]);
  const usuarioId = localStorage.getItem('usuarioId');

  // Buscar notificações do usuário
  useEffect(() => {
    if (!usuarioId) return;

    fetch(`http://localhost:8080/api/notificacoes/usuario/${usuarioId}`)
      .then(res => {
        if (!res.ok) throw new Error('Erro ao buscar notificações');
        return res.json();
      })
      .then(data => setNotificacoes(data))
      .catch(err => console.error(err));
  }, [usuarioId]);

  // Marcar como lida
  const marcarComoLida = (id) => {
    fetch(`http://localhost:8080/api/notificacoes/${id}/lida`, { method: 'PUT' })
      .then(res => {
        if (!res.ok) throw new Error('Erro ao marcar como lida');
        return res.json();
      })
      .then(notifAtualizada => {
        setNotificacoes(prev =>
          prev.map(n => (n.notificacaoId === id ? notifAtualizada : n))
        );
      })
      .catch(err => alert(err.message));
  };

  return (
    <div className="icons" style={{ position: 'relative', cursor: 'pointer' }}>
      <div onClick={() => setAberto(!aberto)}>
        <img
          src="https://img.icons8.com/ios/50/000000/appointment-reminders--v1.png"
          alt="notificação"
        />
        {notificacoes.some(n => !n.lida) && <span className="indicador-notificacao" />}
      </div>

      {aberto && (
        <div
          className="notificacoes-box"
          style={{
            position: 'absolute',
            top: '100%',
            right: 0,
            background: '#fff',
            border: '1px solid #ccc',
            width: 280,
            zIndex: 100,
            boxShadow: '0 2px 6px rgba(0,0,0,0.2)',
            borderRadius: 6
          }}
        >
          <h4 style={{ margin: '10px', fontSize: 16 }}>Notificações</h4>
          <ul style={{ listStyle: 'none', padding: 0, margin: 0 }}>
            {notificacoes.length === 0 && <li style={{ padding: '10px' }}>Sem notificações</li>}
            {notificacoes.map(n => (
              <li
                key={n.notificacaoId}
                style={{
                  padding: '8px 10px',
                  borderBottom: '1px solid #eee',
                  opacity: n.lida ? 0.6 : 1
                }}
              >
                <a
                  href="#!"
                  onClick={e => {
                    e.preventDefault();
                    if (!n.lida) marcarComoLida(n.notificacaoId);
                    // Aqui poderia ter navegação, por exemplo: navigate(n.link)
                  }}
                  style={{
                    color: n.lida ? '#666' : '#000',
                    textDecoration: 'none',
                    fontWeight: n.lida ? 'normal' : 'bold'
                  }}
                  title={n.mensagem}
                >
                  {n.mensagem}
                </a>
                <br />
                <small style={{ fontSize: 10, color: '#999' }}>
                  {n.dataCriacao ? new Date(n.dataCriacao).toLocaleString() : ''}
                </small>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
