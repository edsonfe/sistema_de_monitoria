import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Perfil.css';

export default function Perfil() {
  const navigate = useNavigate();

  const [dados, setDados] = useState({
    usuarioId: null,
    nome: '',
    telefone: '',
    email: '',
    curso: '',
    matricula: '',
    tipo: ''
  });

  const [editando, setEditando] = useState(false);
  const [carregando, setCarregando] = useState(true);
  const [mensagem, setMensagem] = useState('');

  // Pega o ID do usuário logado do localStorage (ajuste se estiver salvando de outra forma)
  const usuarioId = localStorage.getItem('usuarioId');

  // Carrega dados do usuário
  useEffect(() => {
    const fetchUsuario = async () => {
      try {
        const resp = await fetch(`http://localhost:8080/api/usuarios/${usuarioId}`);
        if (!resp.ok) throw new Error('Erro ao buscar dados do usuário.');

        const data = await resp.json();

        setDados({
          usuarioId: data.usuarioId,
          nome: data.nome,
          telefone: data.celular || '',
          email: data.email,
          curso: data.cursoNome,
          matricula: data.matricula,
          tipo: data.tipoUsuario
        });
      } catch (err) {
        setMensagem(err.message);
      } finally {
        setCarregando(false);
      }
    };

    if (usuarioId) fetchUsuario();
  }, [usuarioId]);

  const handleChange = (e) => {
    setDados({ ...dados, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`http://localhost:8080/api/usuarios/${dados.usuarioId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nome: dados.nome,
          email: dados.email,
          senha: '', // você pode tratar senha em outra tela
          celular: dados.telefone,
          dataNascimento: null, // ajuste se tiver esse campo
          matricula: dados.matricula,
          tipoUsuario: dados.tipo,
          cursoId: null // se quiser permitir troca de curso
        })
      });

      if (!response.ok) throw new Error('Erro ao salvar alterações.');

      setMensagem('Dados atualizados com sucesso!');
      setEditando(false);
    } catch (err) {
      setMensagem(err.message);
    }
  };

  if (carregando) return <p>Carregando...</p>;

  return (
    <div className="content perfil-card">
      <div className="voltar-home" onClick={() => navigate(-1, { replace: true })}>
        <img src="https://img.icons8.com/ios-filled/24/03bcd3/left.png" alt="Voltar" />
        <span>Voltar</span>
      </div>

      <h2>Informações do Usuário</h2>
      {mensagem && <p className="mensagem">{mensagem}</p>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Nome</label>
          <input
            type="text"
            name="nome"
            value={dados.nome}
            onChange={handleChange}
            disabled={!editando}
          />
        </div>

        <div className="form-group">
          <label>Telefone</label>
          <input
            type="text"
            name="telefone"
            value={dados.telefone}
            onChange={handleChange}
            disabled={!editando}
          />
        </div>

        <div className="form-group">
          <label>E-mail</label>
          <input type="email" value={dados.email} disabled />
        </div>

        <div className="form-group">
          <label>Curso</label>
          <input type="text" value={dados.curso} disabled />
        </div>

        <div className="form-group">
          <label>Matrícula</label>
          <input type="text" value={dados.matricula} disabled />
        </div>

        <div className="form-group">
          <label>Tipo de Usuário</label>
          <input type="text" value={dados.tipo} disabled />
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
