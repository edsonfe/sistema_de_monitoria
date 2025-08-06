import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/BuscarMonitoria.css';

export default function BuscarMonitoria() {
  const [disciplina, setDisciplina] = useState('');
  const [monitor, setMonitor] = useState('');
  const [resultados, setResultados] = useState([]);
  const [mensagem, setMensagem] = useState('');
  const [carregando, setCarregando] = useState(false);
  const navigate = useNavigate();

  const handleBuscar = async () => {
    const disciplinaTrim = disciplina.trim();
    const monitorTrim = monitor.trim();

    if (!disciplinaTrim && !monitorTrim) {
      setResultados([]);
      setMensagem('Digite algum critério para buscar monitorias.');
      return;
    }

    // Evita buscas muito vagas
    if ((disciplinaTrim && disciplinaTrim.length < 3) || (monitorTrim && monitorTrim.length < 3)) {
      setResultados([]);
      setMensagem('Digite pelo menos 3 letras para buscar.');
      return;
    }

    setCarregando(true);
    setMensagem('');
    setResultados([]);

    try {
      const params = new URLSearchParams();
      if (disciplinaTrim) params.append('disciplina', disciplinaTrim);
      if (monitorTrim) params.append('monitor', monitorTrim);

      const response = await fetch(`http://localhost:8080/api/monitorias?${params.toString()}`);

      if (!response.ok) throw new Error('Erro ao buscar monitorias.');

      const data = await response.json();

      // Filtro extra no front para garantir que contenha o termo
      const resultadosFiltrados = data.filter((item) => {
        const disciplinaMatch = disciplinaTrim
          ? item.disciplina.toLowerCase().includes(disciplinaTrim.toLowerCase())
          : true;
        const monitorMatch = monitorTrim
          ? item.monitorNome.toLowerCase().includes(monitorTrim.toLowerCase())
          : true;

        return disciplinaMatch && monitorMatch;
      });

      if (resultadosFiltrados.length === 0) {
        setMensagem('Nenhuma monitoria encontrada com os critérios informados.');
      } else {
        setResultados(resultadosFiltrados);
      }
    } catch (error) {
      setMensagem(error.message || 'Erro inesperado.');
    } finally {
      setCarregando(false);
    }
  };


  return (
    <div className="content">
      <div className="voltar-home" onClick={() => navigate(-1)} style={{ cursor: 'pointer' }}>
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
        <button type="button" className="btn buscar" onClick={handleBuscar} disabled={carregando}>
          {carregando ? 'Buscando...' : 'Buscar'}
        </button>
      </div>

      <div className="resultado-busca">
        {mensagem && <p>{mensagem}</p>}

        {resultados.map((item) => (
          <div key={item.monitoriaId} className="card-monitoria">
            <div className="info">
              <h3>{item.disciplina}</h3>
              <p>Monitor: {item.monitorNome}</p>
              {/* Você pode adicionar horários e outras informações aqui se existirem */}
            </div>
            <div
              className="acessar"
              onClick={() =>
                navigate(`/detalhe-monitoria/${item.monitoriaId}`, {
                  state: { monitoria: item }
                })
              }
              style={{ cursor: 'pointer' }}
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
