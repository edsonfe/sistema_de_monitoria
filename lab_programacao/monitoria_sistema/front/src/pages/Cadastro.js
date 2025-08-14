import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import imagemCadastro from '../assets/img-cadastro1.png';
import '../styles/Cadastro.css';

export default function Cadastro() {
  const [step, setStep] = useState(1);
  const navigate = useNavigate();

  // Estados do formulário
  const [nome, setNome] = useState('');
  const [celular, setCelular] = useState('');
  const [email, setEmail] = useState('');
  const [dataNascimento, setDataNascimento] = useState('');
  const [cursoId, setCursoId] = useState('');
  const [matricula, setMatricula] = useState('');
  const [tipoUsuario, setTipoUsuario] = useState('ALUNO'); // começa como ALUNO
  const [codigoDeValidacao, setCodigoDeValidacao] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmaSenha, setConfirmaSenha] = useState('');

  const avancar = () => setStep((s) => Math.min(s + 1, 3));
  const voltar = () => setStep((s) => Math.max(s - 1, 1));

  // trecho do método finalizar com validações extras
  const finalizar = async () => {
    if (senha !== confirmaSenha) {
      alert('As senhas não coincidem!');
      return;
    }
    if (!nome || !email || !dataNascimento || !cursoId || !matricula || !senha) {
      alert('Preencha todos os campos obrigatórios!');
      return;
    }
    if (tipoUsuario === 'MONITOR' && !codigoDeValidacao.trim()) {
      alert('Digite o código de validação para Monitor.');
      return;
    }

    const dados = {
      nome,
      celular,
      email,
      dataNascimento,
      cursoId: Number(cursoId),
      matricula,
      senha,
      tipoUsuario,
    };

    try {
      let url = 'http://localhost:8080/api/usuarios';
      if (tipoUsuario === 'MONITOR') {
        url += `?tokenMonitor=${encodeURIComponent(codigoDeValidacao)}`;
      }

      const response = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados),
      });

      if (response.ok) {
        alert('Cadastro realizado com sucesso!');
        navigate('/');
      } else {
        const erro = await response.text();
        alert('Erro ao cadastrar: ' + erro);
      }
    } catch (error) {
      alert('Erro ao conectar com o servidor: ' + error.message);
    }
  };


  return (
    <div className="container">
      <div className="esquerda">
        <form className="form">
          <div className="topo">
            <h2>Cadastro</h2>
            <div className="steps">
              {[1, 2, 3].map((n) => (
                <div className="step" key={n}>
                  <div className={`circle ${step === n ? 'active' : ''}`}>{n}</div>
                  <span>{['Inform. pessoais', 'Curso e tipo', 'Senha'][n - 1]}</span>
                </div>
              ))}
            </div>
          </div>

          {/* Passo 1 - Dados pessoais */}
          {step === 1 && (
            <div className="form-step active">
              <label>Nome</label>
              <input type="text" value={nome} onChange={e => setNome(e.target.value)} required />

              <label>Celular</label>
              <input type="text" value={celular} onChange={e => setCelular(e.target.value)} />

              <label>E-mail</label>
              <input type="email" value={email} onChange={e => setEmail(e.target.value)} required />

              <label>Data de nascimento</label>
              <input type="date" value={dataNascimento} onChange={e => setDataNascimento(e.target.value)} required />

              <div className="btns">
                <button type="button" className="btn cancelar" onClick={() => navigate('/')}>
                  Cancelar
                </button>
                <button type="button" className="btn next" onClick={avancar}>
                  Continuar
                </button>
              </div>
            </div>
          )}

          {/* Passo 2 - Curso e tipo */}
          {step === 2 && (
            <div className="form-step active">
              <label>Curso</label>
              <select value={cursoId} onChange={e => setCursoId(e.target.value)} required>
                <option disabled value="">Selecione o curso</option>
                <option value={1}>Ciência da Computação</option>
              </select>

              <label>Matrícula</label>
              <input type="text" value={matricula} onChange={e => setMatricula(e.target.value)} required />

              <label>Tipo de Usuário</label>
              <select value={tipoUsuario} onChange={e => setTipoUsuario(e.target.value)} required>
                <option value="ALUNO">Aluno</option>
                <option value="MONITOR">Monitor</option>
              </select>

              {tipoUsuario === 'MONITOR' && (
                <>
                  <label>Código de Validação</label>
                  <input type="text" value={codigoDeValidacao} onChange={e => setCodigoDeValidacao(e.target.value)} />
                </>
              )}

              <div className="btns">
                <button type="button" className="btn" onClick={voltar}>
                  Voltar
                </button>
                <button type="button" className="btn next" onClick={avancar}>
                  Continuar
                </button>
              </div>
            </div>
          )}

          {/* Passo 3 - Senha */}
          {step === 3 && (
            <div className="form-step active">
              <label>Senha</label>
              <input type="password" value={senha} onChange={e => setSenha(e.target.value)} required />

              <label>Confirme senha</label>
              <input type="password" value={confirmaSenha} onChange={e => setConfirmaSenha(e.target.value)} required />

              <div className="btns">
                <button type="button" className="btn" onClick={voltar}>
                  Voltar
                </button>
                <button type="button" className="btn next" onClick={finalizar}>
                  Finalizar
                </button>
              </div>
            </div>
          )}
        </form>
      </div>

      <div className="direita">
        <img src={imagemCadastro} alt="Estudante" />
      </div>
    </div>
  );
}
