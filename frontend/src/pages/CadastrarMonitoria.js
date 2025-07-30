import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../styles/CadastrarMonitoria.css';

export default function CadastrarMonitoria() {
  const location = useLocation();
  const navigate = useNavigate();

  const estaEditando = location.pathname.includes('editar-monitoria');
  const dadosSessao = location.state?.sessao || {};

  const [modoEdicao, setModoEdicao] = useState(!estaEditando); // já liberado se for cadastro
  const [mostrarConfirmacao, setMostrarConfirmacao] = useState(false);

  const [form, setForm] = useState({
    curso: dadosSessao.curso || '',
    codigo: dadosSessao.codigo || '',
    disciplina: dadosSessao.titulo || '',
    horario1: dadosSessao.horario1 || '09:00',
    horario2: dadosSessao.horario2 || '10:00',
    dia: dadosSessao.dia || '',
    link: dadosSessao.link || '',
    materiais: dadosSessao.materiais || '',
    observacoes: dadosSessao.observacoes || ''
  });

  const handleChange = (campo, valor) => {
    setForm((prev) => ({ ...prev, [campo]: valor }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Dados salvos:', form);
    setMostrarConfirmacao(true);
  };

  const handleConfirmar = () => {
    setMostrarConfirmacao(false);
    navigate('/home-monitor', { replace: true });
  };

  const bloqueado = estaEditando && !modoEdicao;

  return (
    <div className="content">
      <div className="voltar-home" onClick={() => navigate(-1)}>
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
            value={form.curso}
            onChange={(e) => handleChange('curso', e.target.value)}
            required
            disabled={bloqueado}
          >
            <option>Selecione</option>
            <option>Ciência da Computação</option>
            <option>Engenharia Elétrica</option>
            <option>Medicina</option>
            <option>Direito</option>
            <option>Sociologia</option>
          </select>
        </div>

        <div>
          <label>Código da Disciplina</label>
          <input
            type="text"
            placeholder="Digite o código da disciplina"
            value={form.codigo}
            onChange={(e) => handleChange('codigo', e.target.value)}
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
          <label>Horário da monitoria</label>
          <div style={{ display: 'flex', gap: '5px' }}>
            <input
              type="time"
              value={form.horario1}
              onChange={(e) => handleChange('horario1', e.target.value)}
              required
              disabled={bloqueado}
            />
            <input
              type="time"
              value={form.horario2}
              onChange={(e) => handleChange('horario2', e.target.value)}
              required
              disabled={bloqueado}
            />
          </div>
        </div>

        <div>
          <label>Dia(s) da monitoria</label>
          <input
            type="date"
            value={form.dia}
            onChange={(e) => handleChange('dia', e.target.value)}
            required
            disabled={bloqueado}
          />
        </div>

        <div>
          <label>Link da sala virtual</label>
          <input
            type="url"
            placeholder="Digite o link"
            value={form.link}
            onChange={(e) => handleChange('link', e.target.value)}
            required
            disabled={bloqueado}
          />
        </div>

        <div className="material-box full-width">
          <label>Materiais disponíveis</label>
          <textarea
            rows="3"
            value={form.materiais}
            onChange={(e) => handleChange('materiais', e.target.value)}
            disabled={bloqueado}
          />
        </div>

        <div className="full-width">
          <label>Observações</label>
          <textarea
            rows="3"
            placeholder="Digite a descrição"
            value={form.observacoes}
            onChange={(e) => handleChange('observacoes', e.target.value)}
            disabled={bloqueado}
          />
        </div>

        {(!bloqueado || !estaEditando) && (
          <div className="botoes">
            <button id="bt" type="submit">
              {estaEditando ? 'Salvar alterações' : 'Cadastrar nova monitoria'}
            </button>
            <button id="bt2" type="button" onClick={() => navigate('/home-monitor')}>
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
              {estaEditando ? 'Alterações salvas com sucesso!' : 'Monitoria cadastrada com sucesso!'}
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
