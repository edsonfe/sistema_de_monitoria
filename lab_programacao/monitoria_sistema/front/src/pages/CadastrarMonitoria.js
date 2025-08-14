import { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import MonitoriaForm from './MonitoriaForm';

import '../styles/CadastrarMonitoria.css';

const BASE_URL = 'http://localhost:8080';
const MONITOR_ID_LOGADO = Number(localStorage.getItem('usuarioId'));

export default function CadastrarMonitoria() {
  const navigate = useNavigate();
  const [cursos, setCursos] = useState([]);
  const [loadingCursos, setLoadingCursos] = useState(true);
  const [erroCursos, setErroCursos] = useState(null);
  const [mostrarConfirmacao, setMostrarConfirmacao] = useState(false);

  const formInicial = {
    cursoId: '',
    codigoDisciplina: '',
    disciplina: '',
    diasDaSemana: [],
    horario: '09:00:00',
    linkSalaVirtual: '',
    observacoes: '',
  };

  const carregarCursos = useCallback(async () => {
    try {
      const res = await fetch(`${BASE_URL}/api/cursos`);
      if (!res.ok) throw new Error('Erro ao carregar cursos');
      setCursos(await res.json());
    } catch (e) {
      setErroCursos(e.message);
    } finally {
      setLoadingCursos(false);
    }
  }, []);

  useEffect(() => { carregarCursos(); }, [carregarCursos]);

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
      const res = await fetch(`${BASE_URL}/api/monitorias`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      if (!res.ok) throw new Error(await res.text());
      setMostrarConfirmacao(true);
    } catch (e) { alert('Erro ao cadastrar: ' + e.message); }
  }

  function handleConfirmar() {
    setMostrarConfirmacao(false);
    navigate('/home-monitor', { replace: true });
  }

  return (
    <div className="content">
      <h2>Cadastrar nova monitoria</h2>
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
          <p>Monitoria cadastrada com sucesso!</p>
          <button onClick={handleConfirmar}>OK</button>
        </div>
      }
    </div>
  );
}
