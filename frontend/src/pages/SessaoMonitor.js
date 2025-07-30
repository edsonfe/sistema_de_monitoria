import { useNavigate } from 'react-router-dom';
import '../styles/Sessao.css';

export default function SessaoMonitor() {
  const navigate = useNavigate();

  const horarios = ['09:00', '10:00', '11:00'];
  const dias = ['Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];

  const sessoesMonitor = [
    { dia: 1, hora: '09:00', titulo: 'Cálculo I' },
    { dia: 3, hora: '09:00', titulo: 'Cálculo I' },
    { dia: 4, hora: '10:00', titulo: 'Algoritmos' }
  ];

  const renderCelula = (hora, diaIndex) => {
    const sessao = sessoesMonitor.find(
      (s) => s.hora === hora && s.dia === diaIndex
    );

    if (sessao) {
      return (
        <div className="celula verde">
          <span
            onClick={() => {
              // Agrupa todos os horários da mesma disciplina
              const relacionadas = sessoesMonitor.filter(
                (s) => s.titulo === sessao.titulo
              );

              navigate('/sessao-detalhe-monitor', {
                state: {
                  sessao: {
                    titulo: sessao.titulo,
                    monitor: 'Você',
                    horarios: relacionadas.map(
                      (s) => `${dias[s.dia]} • ${s.hora}`
                    ),
                    link: 'https://link-meet-monitoria',
                    mediaAvaliacao: 4.8,
                    comentarios: [
                      'Explicação clara!',
                      'Didática excelente!',
                      'Sessão muito produtiva!'
                    ]
                  }
                }
              });
            }}
          >
            {sessao.titulo}
          </span>
        </div>
      );
    }

    return <div className="celula"></div>;
  };

  return (
    <div className="content">
      <div className="voltar-home" onClick={() => navigate('/home-monitor')}>
        <img
          src="https://img.icons8.com/ios-filled/24/03bcd3/left.png"
          alt="Voltar"
        />
        <span>Voltar</span>
      </div>

      <h2>Minhas sessões agendadas</h2>

      <div className="grade-horaria">
        <div className="cabecalho">
          <div className="celula-header"></div>
          {dias.map((dia, i) => (
            <div key={i} className="celula-header">
              {dia}
            </div>
          ))}
        </div>

        {horarios.map((hora) => (
          <div key={hora} className="linha-horario">
            <div className="horario">{hora}</div>
            {dias.map((_, diaIndex) => renderCelula(hora, diaIndex))}
          </div>
        ))}
      </div>
    </div>
  );
}
