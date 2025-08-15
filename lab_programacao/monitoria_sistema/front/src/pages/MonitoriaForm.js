import { useState, useEffect } from 'react';

const diasSemanaOptions = [
  { label: 'Seg', valor: 'Segunda' },
  { label: 'Ter', valor: 'Terça' },
  { label: 'Qua', valor: 'Quarta' },
  { label: 'Qui', valor: 'Quinta' },
  { label: 'Sex', valor: 'Sexta' },
  { label: 'Sáb', valor: 'Sábado' },
];

export default function MonitoriaForm({ formInicial, cursos, bloqueado, onSubmit, onCancelar }) {
  const [form, setForm] = useState(formInicial);

  useEffect(() => { setForm(formInicial); }, [formInicial]);

  function handleChange(campo, valor) {
    if (bloqueado) return;
    setForm(prev => ({ ...prev, [campo]: valor }));
  }

  function toggleDia(dia) {
    if (bloqueado) return;
    setForm(prev => {
      const novoDias = prev.diasDaSemana.includes(dia)
        ? prev.diasDaSemana.filter(d => d !== dia)
        : [...prev.diasDaSemana, dia];
      return { ...prev, diasDaSemana: novoDias };
    });
  }

  function handleSubmit(e) {
    e.preventDefault();
    onSubmit(form);
  }

  if (!form) return <p>Carregando formulário...</p>;

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Curso</label>
        <select
          value={form.cursoId}
          onChange={e => handleChange('cursoId', e.target.value)}
          required
          disabled={bloqueado}
        >
          <option value="">Selecione</option>
          {cursos.map(({ cursoId, nome }) => (
            <option key={cursoId} value={cursoId}>{nome}</option>
          ))}
        </select>
      </div>

      <div>
        <label>Código da Disciplina</label>
        <input
          type="text"
          value={form.codigoDisciplina}
          onChange={e => handleChange('codigoDisciplina', e.target.value)}
          required
          disabled={bloqueado}
        />
      </div>

      <div>
        <label>Disciplina</label>
        <input
          type="text"
          value={form.disciplina}
          onChange={e => handleChange('disciplina', e.target.value)}
          required
          disabled={bloqueado}
        />
      </div>

      <div className="full-width">
        <label>Dias da semana</label>
        <div className="checkbox-group">
          {diasSemanaOptions.map(({ label, valor }) => (
            <label key={valor}>
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
          value={form.horario?.slice(0, 5) || '09:00'}
          onChange={e => handleChange('horario', e.target.value)}
          required
          disabled={bloqueado}
        />
      </div>

      <div>
        <label>Link da sala virtual</label>
        <input
          type="url"
          value={form.linkSalaVirtual || ''}
          onChange={e => handleChange('linkSalaVirtual', e.target.value)}
          disabled={bloqueado}
        />
      </div>

      <div className="full-width">
        <label>Observações</label>
        <textarea
          rows={3}
          value={form.observacoes || ''}
          onChange={e => handleChange('observacoes', e.target.value)}
          disabled={bloqueado}
        />
      </div>

      <div className="botoes">
        {!bloqueado && <button type="submit" className="btn-salvar">Salvar</button>}
        <button type="button" className="btn-cancelar" onClick={onCancelar}>Cancelar</button>
      </div>
    </form>
  );
}
