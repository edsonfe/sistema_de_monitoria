import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Perfil.css';

export default function Perfil() {
  const navigate = useNavigate();

  const [dados, setDados] = useState({
    nome: '',
    telefone: '',
    email: '',
    curso: '',
    matricula: '',
    cpf: '',
    tipo: ''
  });

  const [editando, setEditando] = useState(false);

  useEffect(() => {
    // Simulando retorno da API
    const usuario = {
      nome: 'Edson Silva',
      telefone: '(98) 91234-5678',
      email: 'edson@discente.ufma.br',
      curso: 'Engenharia de Software',
      matricula: '2021001234',
      cpf: '000.000.000-00',
      tipo: 'aluno'
    };
    setDados(usuario);
  }, []);

  const handleChange = (e) => {
    setDados({ ...dados, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Dados atualizados com sucesso!\nNome: ${dados.nome}\nTelefone: ${dados.telefone}`);
    setEditando(false);
  };

  return (
    <div className="content perfil">
      <div className="voltar-home" onClick={() => navigate(-1, {replace:true})}>
        <img src="https://img.icons8.com/ios-filled/24/03bcd3/left.png" alt="Voltar" />
        <span>Voltar</span>
      </div>

      <h2>Informações do Usuário</h2>

      <form onSubmit={handleSubmit}>
        <label>Nome</label>
        <input
          type="text"
          name="nome"
          value={dados.nome}
          onChange={handleChange}
          disabled={!editando}
        />

        <label>CPF</label>
        <input type="text" value={dados.cpf} disabled />

        <label>Telefone</label>
        <input
          type="text"
          name="telefone"
          value={dados.telefone}
          onChange={handleChange}
          disabled={!editando}
        />

        <label>E-mail</label>
        <input type="email" value={dados.email} disabled />

        <label>Curso</label>
        <input type="text" value={dados.curso} disabled />

        <label>Matrícula</label>
        <input type="text" value={dados.matricula} disabled />

        <div className="botoes">
          {!editando && (
            <button type="button" onClick={() => setEditando(true)}>
              Editar
            </button>
          )}
          <button type="submit">Salvar</button>
        </div>
      </form>
    </div>
  );
}
