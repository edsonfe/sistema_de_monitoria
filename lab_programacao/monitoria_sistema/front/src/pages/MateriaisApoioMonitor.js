import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../styles/MateriaisApoioMonitor.css';

export default function MateriaisApoioMonitor() {
  const { state } = useLocation();
  const sessaoId = state?.sessaoId;
  const navigate = useNavigate();
  const [materiais, setMateriais] = useState([]);
  const [form, setForm] = useState({ titulo: '', link: '', arquivo: null });
  const [loading, setLoading] = useState(false);

  const handleInput = (campo, valor) => {
    setForm(prev => ({ ...prev, [campo]: valor }));
  };

  const adicionarMaterial = () => {
    if (!form.titulo.trim()) {
      alert('Título é obrigatório');
      return;
    }

    // Apenas para controle no front
    const novoMaterial = {
      tempId: Date.now(),
      titulo: form.titulo.trim(),
      link: form.link.trim() || null,
      arquivo: form.arquivo ? form.arquivo.name : null
    };

    setMateriais(prev => [...prev, novoMaterial]);
    setForm({ titulo: '', link: '', arquivo: null });
  };

  const removerMaterial = tempId => {
    setMateriais(prev => prev.filter(m => m.tempId !== tempId));
  };

  const salvarTodos = async () => {
    if (!sessaoId) {
      alert('Sessão inválida.');
      return;
    }
    if (materiais.length === 0) {
      alert('Nenhum material para enviar.');
      return;
    }

    setLoading(true);
    try {
      for (const mat of materiais) {
        const body = {
          titulo: mat.titulo,
          link: mat.link, // pode ser null
          sessaoId: sessaoId
        };

        const res = await fetch('http://localhost:8080/api/materiais', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(body)
        });

        if (!res.ok) {
          let errorMsg = 'Erro ao salvar material';
          try {
            const errorData = await res.json();
            errorMsg = errorData.message || errorData;
          } catch { }
          throw new Error(errorMsg);
        }
      }
      alert('Materiais enviados com sucesso!');
      navigate('/sessao-monitor');
    } catch (error) {
      alert('Erro ao enviar materiais: ' + error.message);
    } finally {
      setLoading(false);
    }
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

      <h2>Adicionar material</h2>

      <div className="formulario-material">
        <label>Título</label>
        <input
          type="text"
          placeholder="Digite o título do material"
          value={form.titulo}
          onChange={e => handleInput('titulo', e.target.value)}
        />

        <label>Arquivo (opcional, apenas exibido)</label>
        <input
          type="file"
          onChange={e => handleInput('arquivo', e.target.files[0])}
        />

        <label>Link (opcional)</label>
        <input
          type="url"
          placeholder="Coloque o link para um material externo"
          value={form.link}
          onChange={e => handleInput('link', e.target.value)}
        />

        <button className="btn-adicionar" onClick={adicionarMaterial} disabled={loading}>
          Adicionar
        </button>
      </div>

      <div className="lista-materiais">
        {materiais.map(mat => (
          <div key={mat.tempId} className="item-material">
            <strong>{mat.titulo}</strong>
            {mat.arquivo && <p>📎 {mat.arquivo}</p>}
            {mat.link && (
              <p>
                🔗{' '}
                <a href={mat.link} target="_blank" rel="noreferrer">
                  {mat.link}
                </a>
              </p>
            )}
            <button onClick={() => removerMaterial(mat.tempId)} disabled={loading}>
              Remover
            </button>
          </div>
        ))}
      </div>

      {materiais.length > 0 && (
        <button className="btn-salvar" onClick={salvarTodos} disabled={loading}>
          {loading ? 'Enviando...' : 'Salvar'}
        </button>
      )}
    </div>
  );
}
