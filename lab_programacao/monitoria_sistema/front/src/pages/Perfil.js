import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Perfil.css';

export default function Perfil() {
  const navigate = useNavigate();
  const usuarioId = localStorage.getItem('usuarioId');

  const [dados, setDados] = useState({
    usuarioId: null,
    nome: '',
    celular: '',
    email: '',
    cursoNome: '',
    cursoId: null,
    matricula: '',
    tipoUsuario: '',
    senha: '',           // Adicionado para compatibilidade com PUT
    dataNascimento: ''   // Adicionado para compatibilidade com PUT
  });

  const [editando, setEditando] = useState(false);
  const [carregando, setCarregando] = useState(true);
  const [mensagem, setMensagem] = useState('');

  useEffect(() => {
    if (!usuarioId) return;

    const fetchUsuario = async () => {
      try {
        const resp = await fetch(`http://localhost:8080/api/usuarios/${usuarioId}`);
        if (!resp.ok) throw new Error('Erro ao buscar dados do usuário.');
        const data = await resp.json();

        setDados({
          usuarioId: data.usuarioId,
          nome: data.nome,
          celular: data.celular || '',
          email: data.email,
          cursoNome: data.cursoNome || '',
          cursoId: data.cursoId || null,
          matricula: data.matricula || '',
          tipoUsuario: data.tipoUsuario || '',
          senha: '',                // Mantemos vazio por segurança
          dataNascimento: data.dataNascimento || ''
        });
      } catch (err) {
        setMensagem(err.message);
      } finally {
        setCarregando(false);
      }
    };

    fetchUsuario();
  }, [usuarioId]);

  const handleChange = (e) => {
    setDados({ ...dados, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMensagem('');
    try {
      const response = await fetch(`http://localhost:8080/api/usuarios/${dados.usuarioId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nome: dados.nome,
          celular: dados.celular,
          email: dados.email,
          matricula: dados.matricula,
          tipoUsuario: dados.tipoUsuario,
          cursoId: dados.cursoId,
          senha: dados.senha,
          dataNascimento: dados.dataNascimento
        })
      });

      if (!response.ok) {
        const errData = await response.json();
        throw new Error(errData?.message || 'Erro ao salvar alterações.');
      }
      setMensagem('Dados atualizados com sucesso!');
      setEditando(false);
    } catch (err) {
      setMensagem(err.message);
    }
  };

  if (carregando) return <p>Carregando...</p>;

  return (
    <div className="content perfil-card">
      <div className="voltar-home" onClick={() => navigate(-1)}>
        <img src="https://img.icons8.com/ios-filled/24/03bcd3/left.png" alt="Voltar" />
        <span>Voltar</span>
      </div>

      <h2>Informações do Usuário</h2>
      {mensagem && <p className="mensagem">{mensagem}</p>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Nome</label>
          <input type="text" name="nome" value={dados.nome} onChange={handleChange} disabled={!editando} />
        </div>

        <div className="form-group">
          <label>Telefone</label>
          <input type="text" name="celular" value={dados.celular} onChange={handleChange} disabled={!editando} />
        </div>

        <div className="form-group">
          <label>E-mail</label>
          <input type="email" name="email" value={dados.email} disabled />
        </div>

        <div className="form-group">
          <label>Curso</label>
          <input type="text" value={dados.cursoNome} disabled />
        </div>

        <div className="form-group">
          <label>Matrícula</label>
          <input type="text" value={dados.matricula} disabled />
        </div>

        <div className="form-group">
          <label>Tipo de Usuário</label>
          <input type="text" value={dados.tipoUsuario} disabled />
        </div>

        <div className="botoes">
          {!editando && (
            <button type="button" className="btn-edit" onClick={() => setEditando(true)}>
              Editar
            </button>
          )}
          {editando && <button type="submit" className="btn-save">Salvar</button>}
        </div>
      </form>
    </div>
  );
}
