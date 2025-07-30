import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import imagemCadastro from '../assets/img-cadastro1.png';
import '../styles/Cadastro.css';

export default function Cadastro() {
  const [step, setStep] = useState(1);
  const navigate = useNavigate();

  // Estados para os campos do formulário
  const [nome, setNome] = useState('');
  const [celular, setCelular] = useState('');
  const [email, setEmail] = useState('');
  const [dataNascimento, setDataNascimento] = useState('');
  const [curso, setCurso] = useState('');
  const [matricula, setMatricula] = useState('');
  const [cadeiras, setCadeiras] = useState([]);
  const [tipoUsuario] = useState('');
  const [codigoDeValidacao, setCodigoDeValidacao] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmaSenha, setConfirmaSenha] = useState('');

  const avancar = () => setStep((s) => Math.min(s + 1, 3));
  const voltar = () => setStep((s) => Math.max(s - 1, 1));

  const finalizar = async () => {
    if (senha !== confirmaSenha) {
      alert('As senhas não coincidem!');
      return;
    }

    const dados = {
      nome,
      celular,
      email,
      dataNascimento,
      curso,
      matricula,
      cadeiras,
      senha,
      tipoUsuario,
      codigoDeValidacao
    };

    try {
      const response = await fetch('http://localhost:8080/api/usuarios', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados),
      });

      if (response.ok) {
        navigate('/monitorias');
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
                  <span>{['Inform. pessoais', 'Preferências', 'Senha'][n - 1]}</span>
                </div>
              ))}
            </div>
          </div>

          {/* Passo 1 */}
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

          {/* Passo 2 */}
          {step === 2 && (
            <div className="form-step active">
              <label>Curso</label>
              <select value={curso} onChange={e => setCurso(e.target.value)} required>
                <option disabled value="">Selecione o curso</option>
                <option>Ciência da computação</option>
              </select>

              <label>Matrícula</label>
              <input type="text" value={matricula} onChange={e => setMatricula(e.target.value)} required />

              <label>Cadeiras matriculado</label>
              <div className="cadeiras-input">
                <input
                  type="text"
                  placeholder="Digite o nome da cadeira e pressione Enter"
                  onKeyDown={(e) => {
                    if (e.key === 'Enter' && e.target.value.trim()) {
                      e.preventDefault();
                      setCadeiras([...cadeiras, e.target.value.trim()]);
                      e.target.value = '';
                    }
                  }}
                />
                <ul className="cadeiras-lista">
                  {cadeiras.map((c, index) => (
                    <li key={index}>
                      {c}
                      <button type="button" onClick={() => {
                        setCadeiras(cadeiras.filter((_, i) => i !== index));
                      }}>✖</button>
                    </li>
                  ))}
                </ul>
              </div>

              <label>Código de Validação</label>
              <input type="text" value={codigoDeValidacao} onChange={e => setCodigoDeValidacao(e.target.value)} required />

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

          {/* Passo 3 */}
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
