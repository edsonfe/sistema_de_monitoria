import { useNavigate } from 'react-router-dom';
import './styles.css';
import logo from '../../assets/logo1.ico';

const monitorias = [
  {
    id: 1,
    nome: 'Monitor 01',
    disciplina: 'Estrutura de dados I',
    horario: 'seg: 10:00 - 11:00 | qua: 10:00 - 12:00',
  },
  {
    id: 2,
    nome: 'Monitor 02',
    disciplina: 'Linguagem de programação',
    horario: 'ter: 10:00 - 11:00 | qui: 10:00 - 12:00',
  },
  {
    id: 3,
    nome: 'Monitor 03',
    disciplina: 'Redes de computadores',
    horario: 'sex: 10:00 - 11:00 | sáb: 10:00 - 12:00',
  },
];

const Agendamento = () => {
  const navigate = useNavigate();

  const finalizar = () => {
    navigate('/home-aluno');
  };

  return (
    <div className="agendamento-container">
      <header>
        <img src={logo} alt="Logo padrão" className="logo-img" width={100} />
      </header>

      <h2>Monitorias Disponíveis</h2>

      <div className="search-box">
        <input type="text" placeholder="Buscar monitoria" />
        <span className="search-icon">🔍</span>
      </div>

      <div className="monitor-list">
        {monitorias.map((m) => (
          <div key={m.id} className="monitor-card">
            <div>
              <strong>{m.nome}</strong><br />
              {m.disciplina}<br />
              <small>{m.horario}</small>
            </div>
            <button>Agendar</button>
          </div>
        ))}
      </div>

      <button className="btn-avancar" onClick={finalizar}>
        Avançar
      </button>
    </div>
  );
};

export default Agendamento;
