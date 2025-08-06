import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../styles/CadastrarMonitoria.css';

const diasSemanaOptions = [
  { label: 'Seg', valor: 'Segunda' },
  { label: 'Ter', valor: 'Terça' },
  { label: 'Qua', valor: 'Quarta' },
  { label: 'Qui', valor: 'Quinta' },
  { label: 'Sex', valor: 'Sexta' },
  { label: 'Sáb', valor: 'Sábado' },
];

const cursosSimulados = [
  { cursoId: 1, nome: 'Ciência da Computação' }
];

const MONITOR_ID_LOGADO = 123;

export default function CadastrarMonitoria() {
  const location = useLocation();
  const navigate = useNavigate();

  const estaEditando = location.pathname.includes('editar-monitoria');
  const dadosSessao = location.state?.sessao || {};

  const [modoEdicao, setModoEdicao] = useState(!estaEditando);
  const [mostrarConfirmacao, setMostrarConfirmacao] = useState(false);

  const [form, setForm] = useState({
    cursoId: dadosSessao.cursoId || '',
    codigoDisciplina: dadosSessao.codigoDisciplina || '',
    disciplina: dadosSessao.disciplina || '',
    diasDaSemana: dadosSessao.diasDaSemana
      ? dadosSessao.diasDaSemana.split(',').map((d) => d.trim())
      : [],
    // Garante o padrão "HH:mm:ss" para o backend
    horario: dadosSessao.horario
      ? (dadosSessao.horario.length === 5 ? dadosSessao.horario + ':00' : dadosSessao.horario)
      : '09:00:00',
    linkSalaVirtual: dadosSessao.linkSalaVirtual || '',
    observacoes: dadosSessao.observacoes || '',
  });

  const bloqueado = estaEditando && !modoEdicao;

  function toggleDia(dia) {
    if (bloqueado) return;
    setForm((prev) => {
      const dias = prev.diasDaSemana.includes(dia)
        ? prev.diasDaSemana.filter((d) => d !== dia)
        : [...prev.diasDaSemana, dia];
      return { ...prev, diasDaSemana: dias };
    });
  }

  const handleChange = (campo, valor) => {
    if (bloqueado) return;
    setForm((prev) => ({ ...prev, [campo]: valor }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (form.diasDaSemana.length === 0) {
      alert('Selecione pelo menos um dia da semana.');
      return;
    }

    // Ajusta horário para o formato "HH:mm:ss"
    const horarioFormatado = form.horario.length === 5 ? form.horario + ':00' : form.horario;

    const payload = {
      disciplina: form.disciplina,
      codigoDisciplina: form.codigoDisciplina,
      diasDaSemana: form.diasDaSemana.join(', '),
      horario: horarioFormatado,
      linkSalaVirtual: form.linkSalaVirtual,
      observacoes: form.observacoes,
      monitorId: MONITOR_ID_LOGADO,
      cursoId: Number(form.cursoId),
    };

    try {
      const url = estaEditando
        ? `/api/monitorias/${dadosSessao.monitoriaId}`
        : '/api/monitorias';
      const method = estaEditando ? 'PUT' : 'POST';

      const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorMsg = await response.text();
        throw new Error(errorMsg);
      }

      setMostrarConfirmacao(true);
    } catch (error) {
      alert('Erro ao salvar monitoria: ' + error.message);
    }
  };

  const handleConfirmar = () => {
    setMostrarConfirmacao(false);
    navigate('/home-monitor', { replace: true });
  };

  return (
    <div className="content">
      <div className="voltar-home" onClick={() => navigate(-1)} style={{ cursor: 'pointer' }}>
        <img
          src="https://img.icons8.com/ios-filled/24/03bcd3/left.png"
          alt="Voltar"
        />
        <span style={{ marginLeft: '4px' }}>Voltar</span>
      </div>

      <h2>{estaEditando ? 'Editar monitoria' : 'Cadastrar nova monitoria'}</h2>

      {bloqueado && (
        <button className="btn-editar" onClick={() => setModoEdicao(true)}>
          Editar
        </button>
      )}

      <form onSubmit={handleSubmit}>
        <div>
          <label>Curso</label>
          <select
            value={form.cursoId}
            onChange={(e) => handleChange('cursoId', e.target.value)}
            required
            disabled={bloqueado}
          >
            <option value="">Selecione</option>
            {cursosSimulados.map(({ cursoId, nome }) => (
              <option key={cursoId} value={cursoId}>
                {nome}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label>Código da Disciplina</label>
          <input
            type="text"
            placeholder="Digite o código da disciplina"
            value={form.codigoDisciplina}
            onChange={(e) => handleChange('codigoDisciplina', e.target.value)}
            required
            disabled={bloqueado}
          />
        </div>

        <div>
          <label>Disciplina</label>
          <input
            type="text"
            placeholder="Digite o nome da disciplina"
            value={form.disciplina}
            onChange={(e) => handleChange('disciplina', e.target.value)}
            required
            disabled={bloqueado}
          />
        </div>

        <div>
          <label>Dias da semana</label>
          <div style={{ display: 'flex', gap: '10px' }}>
            {diasSemanaOptions.map(({ label, valor }) => (
              <label key={valor} style={{ userSelect: 'none' }}>
                <input
                  type="checkbox"
                  checked={form.diasDaSemana.includes(valor)}
                  onChange={() => toggleDia(valor)}
                  disabled={bloqueado}
                />
                {label}
              </label>
            ))}
          </div>
        </div>

        <div>
          <label>Horário da monitoria</label>
          <input
            type="time"
            value={form.horario.slice(0, 5)} // mostra só HH:mm no input
            onChange={(e) => handleChange('horario', e.target.value)}
            required
            disabled={bloqueado}
          />
        </div>

        <div>
          <label>Link da sala virtual</label>
          <input
            type="url"
            placeholder="Digite o link"
            value={form.linkSalaVirtual}
            onChange={(e) => handleChange('linkSalaVirtual', e.target.value)}
            disabled={bloqueado}
          />
        </div>

        <div className="material-box full-width">
          <label>Materiais disponíveis (opcional)</label>
          <textarea
            rows="3"
            value={form.observacoes}
            onChange={(e) => handleChange('observacoes', e.target.value)}
            disabled={bloqueado}
            placeholder="Digite observações ou materiais"
          />
        </div>

        {(!bloqueado || !estaEditando) && (
          <div className="botoes">
            <button id="bt" type="submit">
              {estaEditando ? 'Salvar alterações' : 'Cadastrar nova monitoria'}
            </button>
            <button
              id="bt2"
              type="button"
              onClick={() => navigate('/home-monitor')}
            >
              Cancelar
            </button>
          </div>
        )}
      </form>

      {mostrarConfirmacao && (
        <div className="confirm-box">
          <div className="confirm-content">
            <img
              src="https://img.icons8.com/emoji/48/check-mark-emoji.png"
              alt="Confirmado"
              style={{ width: '50px', marginBottom: '15px' }}
            />
            <p>
              {estaEditando
                ? 'Alterações salvas com sucesso!'
                : 'Monitoria cadastrada com sucesso!'}
            </p>
            <button id="bt3" onClick={handleConfirmar}>
              OK
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
