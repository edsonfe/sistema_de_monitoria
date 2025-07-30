import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/BuscarMonitoria.css';

export default function BuscarMonitoria() {
  const [disciplina, setDisciplina] = useState('');
  const [monitor, setMonitor] = useState('');
  const [resultados, setResultados] = useState([]);
  const [mensagem, setMensagem] = useState('');
  const navigate = useNavigate();

  const handleBuscar = () => {
    if (!disciplina.trim() && !monitor.trim()) {
      setResultados([]);
      setMensagem('Digite algum critério para buscar monitorias.');
      return;
    }

    // Simulando retorno de API com horários
    setResultados([
      {
        id: 1,
        disciplina: 'Laboratório de Programação',
        monitor: 'João Silva',
        horarios: ['Segunda • 09:00', 'Quarta • 11:00'],
      }
    ]);
    setMensagem('');
  };

  return (
    <div className="content">
      <div className="voltar-home" onClick={() => navigate(-1)}>
        <img src="https://img.icons8.com/ios-filled/24/03bcd3/left.png" alt="Voltar" />
        <span style={{ marginLeft: '3px' }}>Voltar</span>
      </div>

      <h2>Buscar monitoria</h2>

      <div className="filtros-busca">
        <input
          type="text"
          placeholder="Buscar por disciplina..."
          value={disciplina}
          onChange={(e) => setDisciplina(e.target.value)}
        />
        <input
          type="text"
          placeholder="Buscar por nome do monitor..."
          value={monitor}
          onChange={(e) => setMonitor(e.target.value)}
        />
        <button type='button' className='btn buscar' onClick={handleBuscar}>Buscar</button>
      </div>

      <div className="resultado-busca">
        {mensagem && <p>{mensagem}</p>}
        {resultados.map((item) => (
          <div key={item.id} className="card-monitoria">
            <div className="info">
              <h3>{item.disciplina}</h3>
              <p>Monitor: {item.monitor}</p>
            </div>
            <div
              className="acessar"
              onClick={() =>
                navigate(`/detalhe-monitoria/${item.id}`, {
                  state: { monitoria: item }
                })
              }
            >
              <img
                src="https://img.icons8.com/ios-filled/24/000000/view-details.png"
                alt="Ver detalhes"
              />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
