import { useNavigate } from 'react-router-dom';
import '../styles/SessaoAluno.css';

export default function SessaoAluno() {
  const navigate = useNavigate();

  const horarios = ['09:00', '10:00', '11:00'];
  const dias = ['Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];

  // Sessões simuladas
  const sessoes = [
    { dia: 2, hora: '10:00', titulo: 'ED I' },
    { dia: 4, hora: '10:00', titulo: 'ED I' }
  ];

  const renderCelula = (hora, diaIndex) => {
    const sessao = sessoes.find(s => s.hora === hora && s.dia === diaIndex);

    if (sessao) {
      return (
        <div className="celula verde">
          <span
            onClick={() =>
              navigate('/sessao-detalhe', {
                state: {
                  sessao: {
                    titulo: 'Estrutura de Dados I',
                    monitor: 'Nome Monitor',
                    horarios: ['Terça • 11:00', 'Quinta • 11:00'],
                    link: 'https://linkdareunião.meet'
                  }
                }
              })
            }
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
      <div className="voltar-home" onClick={() => navigate(-1, { replace: true })}>
        <img src="https://img.icons8.com/ios-filled/24/03bcd3/left.png" alt="Voltar" />
        <span>Voltar</span>
      </div>

      <h2>Calendário de monitoria</h2>

      <div className="grade-horaria">
        <div className="cabecalho">
          <div className="celula-header"></div>
          {dias.map((dia, i) => (
            <div key={i} className="celula-header">{dia}</div>
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
