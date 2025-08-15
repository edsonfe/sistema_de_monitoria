import { useState, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../styles/MateriaisApoioMonitor.css';

export default function MateriaisApoioMonitor() {
  const { state } = useLocation();
  const sessaoId = state?.sessaoId;
  const navigate = useNavigate();

  const arquivoInputRef = useRef();

  const [materiais, setMateriais] = useState([]);
  const [form, setForm] = useState({ titulo: '', link: '', arquivo: null });
  const [loading, setLoading] = useState(false);

  // Atualiza campos do formulário
  const handleInput = (campo, valor) => setForm(prev => ({ ...prev, [campo]: valor }));

  // Adiciona material à lista temporária
  const adicionarMaterial = () => {
    if (!form.titulo.trim()) {
      alert('Título é obrigatório');
      return;
    }

    setMateriais(prev => [
      ...prev,
      {
        tempId: Date.now(),
        titulo: form.titulo.trim(),
        link: form.link.trim() || null,
        arquivo: form.arquivo || null,
      }
    ]);

    setForm({ titulo: '', link: '', arquivo: null });
    if (arquivoInputRef.current) arquivoInputRef.current.value = '';
  };

  // Remove material da lista temporária
  const removerMaterial = tempId => setMateriais(prev => prev.filter(m => m.tempId !== tempId));

  // Salva todos os materiais enviando ao backend
  const salvarTodos = async () => {
    if (!sessaoId) return alert('Sessão inválida.');
    if (materiais.length === 0) return alert('Nenhum material para enviar.');

    setLoading(true);

    try {
      const novosMateriais = [];

      for (const mat of materiais) {
        const formData = new FormData();
        formData.append('titulo', mat.titulo);
        formData.append('sessaoId', String(sessaoId));
        if (mat.link) formData.append('link', mat.link);
        if (mat.arquivo instanceof File) formData.append('arquivo', mat.arquivo);

        const res = await fetch('http://localhost:8080/api/materiais/upload', {
          method: 'POST',
          body: formData,
        });

        if (!res.ok) {
          const errorData = await res.json().catch(() => ({}));
          throw new Error(errorData.message || 'Erro ao enviar material');
        }

        const data = await res.json();
        novosMateriais.push(data); // já vem com arquivoUrl do backend
      }

      setMateriais(novosMateriais); // atualiza lista com URLs corretas
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
      {/* Voltar */}
      <div className="voltar-home" onClick={() => navigate(-1)} style={{ cursor: 'pointer' }}>
        <img src="https://img.icons8.com/ios-filled/24/03bcd3/left.png" alt="Voltar" />
        <span style={{ marginLeft: '4px' }}>Voltar</span>
      </div>

      <h2>Adicionar material</h2>

      {/* Formulário */}
      <div className="formulario-material">
        <label>Título</label>
        <input
          type="text"
          placeholder="Digite o título do material"
          value={form.titulo}
          onChange={e => handleInput('titulo', e.target.value)}
        />

        <label>Arquivo (opcional)</label>
        <input
          type="file"
          ref={arquivoInputRef}
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

      {/* Lista de materiais temporários */}
      <div className="lista-materiais">
        {materiais.map(mat => (
          <div key={mat.materialId ?? mat.tempId}
 className="item-material">
            <strong>{mat.titulo}</strong>
            {mat.arquivo && (
              <p>
                📎 {mat.arquivoUrl
                  ? <a href={mat.arquivoUrl} target="_blank" rel="noreferrer">{mat.arquivo}</a>
                  : mat.arquivo.name
                }
              </p>
            )}

            {mat.link && (
              <p>
                🔗 <a href={mat.link} target="_blank" rel="noreferrer">{mat.link}</a>
              </p>
            )}
            <button onClick={() => removerMaterial(mat.tempId)} disabled={loading}>
              Remover
            </button>
          </div>
        ))}
      </div>

      {/* Botão salvar todos */}
      {materiais.length > 0 && (
        <button className="btn-salvar" onClick={salvarTodos} disabled={loading}>
          {loading ? 'Enviando...' : 'Salvar'}
        </button>
      )}
    </div>
  );
}
