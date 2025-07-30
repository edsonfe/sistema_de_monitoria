import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/MateriaisApoioMonitor.css';

export default function MateriaisApoioMonitor() {
  const navigate = useNavigate();
  const [materiais, setMateriais] = useState([]);
  const [form, setForm] = useState({ titulo: '', link: '', arquivo: null });

  const handleInput = (campo, valor) => {
    setForm((prev) => ({ ...prev, [campo]: valor }));
  };

  const adicionarMaterial = () => {
    if (!form.titulo) return;

    const novoMaterial = {
      id: Date.now(),
      titulo: form.titulo,
      link: form.link,
      arquivo: form.arquivo?.name || null
    };

    setMateriais((prev) => [...prev, novoMaterial]);
    setForm({ titulo: '', link: '', arquivo: null });
  };

  const removerMaterial = (id) => {
    setMateriais((prev) => prev.filter((m) => m.id !== id));
  };

  const salvarTodos = () => {
    console.log('Materiais salvos:', materiais);
    navigate('/sessao-monitor');
  };

  return (
    <div className="content">
      <div className="voltar-home" onClick={() => navigate(-1)}>
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
          onChange={(e) => handleInput('titulo', e.target.value)}
        />

        <label>Arquivo</label>
        <input
          type="file"
          onChange={(e) => handleInput('arquivo', e.target.files[0])}
        />

        <label>Link</label>
        <input
          type="url"
          placeholder="Coloque o link para um material externo"
          value={form.link}
          onChange={(e) => handleInput('link', e.target.value)}
        />

        <button className="btn-adicionar" onClick={adicionarMaterial}>
          Adicionar
        </button>
      </div>

      <div className="lista-materiais">
        {materiais.map((mat) => (
          <div key={mat.id} className="item-material">
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
            <button onClick={() => removerMaterial(mat.id)}>Remover</button>
          </div>
        ))}
      </div>

      {materiais.length > 0 && (
        <button className="btn-salvar" onClick={salvarTodos}>
          Salvar
        </button>
      )}
    </div>
  );
}
