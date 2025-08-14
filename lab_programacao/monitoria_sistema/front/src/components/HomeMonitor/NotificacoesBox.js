import { useState, useEffect } from 'react';

export default function NotificacoesBox() {
  const [aberto, setAberto] = useState(false);
  const [notificacoes, setNotificacoes] = useState([]);
  const usuarioId = localStorage.getItem('usuarioId');

  useEffect(() => {
    if (!usuarioId) return;

    fetch(`http://localhost:8080/api/notificacoes/usuario/${usuarioId}`)
      .then(res => res.json())
      .then(data => setNotificacoes(data))
      .catch(err => console.error('Erro ao buscar notificações:', err));
  }, [usuarioId]);

  const marcarComoLida = (id) => {
    fetch(`http://localhost:8080/api/notificacoes/${id}/lida`, { method: 'PUT' })
      .then(res => {
        if (!res.ok) throw new Error('Erro ao marcar notificação como lida');
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
    <div className="icons" onClick={() => setAberto(!aberto)} style={{ position: 'relative', cursor: 'pointer' }}>
      <img
        src="https://img.icons8.com/ios/50/000000/appointment-reminders--v1.png"
        alt="notificação"
      />
      {notificacoes.some(n => !n.lida) && <span className="indicador-notificacao" />}
      {aberto && (
        <div className="notificacoes-box" style={{ position: 'absolute', top: '100%', right: 0, background: '#fff', border: '1px solid #ccc', width: 250, zIndex: 100 }}>
          <h4>Notificações</h4>
          <ul style={{ listStyle: 'none', padding: 0 }}>
            {notificacoes.length === 0 && <li>Sem notificações</li>}
            {notificacoes.map(n => (
              <li key={n.notificacaoId} style={{ opacity: n.lida ? 0.5 : 1, marginBottom: 8 }}>
                <a
                  href="#!"
                  onClick={e => {
                    e.preventDefault();
                    if (!n.lida) marcarComoLida(n.notificacaoId);
                    // Aqui você pode também navegar para onde quiser, ex:
                    // navigate('/alguma-rota');
                  }}
                  style={{ color: n.lida ? '#666' : '#000', textDecoration: 'none' }}
                  title={n.mensagem}
                >
                  {n.mensagem}
                </a>
                <br />
                <small style={{ fontSize: 10, color: '#999' }}>
                  {n.dataCriacao ? new Date(n.dataCriacao).toLocaleString() : 'Data inválida'}
                </small>


              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
