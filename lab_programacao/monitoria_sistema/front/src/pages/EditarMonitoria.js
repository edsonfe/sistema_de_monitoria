import { useEffect, useState, useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import MonitoriaForm from './MonitoriaForm';
const BASE_URL = 'http://localhost:8080';
const MONITOR_ID_LOGADO = Number(localStorage.getItem('usuarioId'));

export default function EditarMonitoria() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [formInicial, setFormInicial] = useState(null);
  const [cursos, setCursos] = useState([]);
  const [loadingCursos, setLoadingCursos] = useState(true);
  const [erroCursos, setErroCursos] = useState(null);
  const [mostrarConfirmacao, setMostrarConfirmacao] = useState(false);

  const carregarCursos = useCallback(async () => {
    try {
      const res = await fetch(`${BASE_URL}/api/cursos`);
      if (!res.ok) throw new Error('Erro ao carregar cursos');
      setCursos(await res.json());
    } catch (e) {
      setErroCursos(e.message);
    } finally { setLoadingCursos(false); }
  }, []);

  const carregarMonitoria = useCallback(async () => {
    try {
      const res = await fetch(`${BASE_URL}/api/monitorias/${id}`); // Ajuste o ID conforme necessário
      if (!res.ok) throw new Error('Erro ao carregar monitoria');
      const dados = await res.json();
      setFormInicial({
        cursoId: dados.cursoId ?? '',
        codigoDisciplina: dados.codigoDisciplina ?? '',
        disciplina: dados.disciplina ?? '',
        diasDaSemana: dados.diasDaSemana ? dados.diasDaSemana.split(',').map(d => d.trim()) : [],
        horario: dados.horario?.length === 5 ? dados.horario + ':00' : dados.horario || '09:00:00',
        linkSalaVirtual: dados.linkSalaVirtual ?? '',
        observacoes: dados.observacoes ?? '',
      });
    } catch (e) { alert(e.message); }
  }, [id]);

  useEffect(() => { carregarCursos(); carregarMonitoria(); }, [carregarCursos, carregarMonitoria]);

  async function handleSubmit(form) {
    const payload = {
      ...form,
      diasDaSemana: form.diasDaSemana.join(', '),
      horario: form.horario.length === 5 ? form.horario + ':00' : form.horario,
      monitorId: MONITOR_ID_LOGADO,
      cursoId: Number(form.cursoId),
      linkSalaVirtual: form.linkSalaVirtual || null,
      observacoes: form.observacoes || null,
    };
    try {
      const res = await fetch(`${BASE_URL}/api/monitorias/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });// Ajuste o ID conforme necessário
      if (!res.ok) throw new Error(await res.text());
      setMostrarConfirmacao(true);
    } catch (e) { alert('Erro ao atualizar: ' + e.message); }
  }

  function handleConfirmar() {
    setMostrarConfirmacao(false);
    navigate('/home-monitor', { replace: true });
  }

  if (!formInicial) return <p>Carregando monitoria...</p>;

  return (
    <div className="content">
      <h2>Editar monitoria</h2>
      {loadingCursos ? <p>Carregando cursos...</p> :
        erroCursos ? <p style={{ color: 'red' }}>Erro: {erroCursos}</p> :
        <MonitoriaForm
          formInicial={formInicial}
          cursos={cursos}
          bloqueado={false}
          onSubmit={handleSubmit}
          onCancelar={() => navigate('/home-monitor')}
        />
      }
      {mostrarConfirmacao &&
        <div className="confirm-box">
          <p>Monitoria atualizada com sucesso!</p>
          <button onClick={handleConfirmar}>OK</button>
        </div>
      }
    </div>
  );
}
