import './styles.css';
import { useNavigate } from 'react-router-dom';
import logo from '../../assets/logo1.ico';

const HomeAluno = () => {
  const navigate = useNavigate();

  return (
    <div className="container">
      <aside className="sidebar">
        <div className='logo-fixa'>
          <img src={logo} alt="Logo" className="logo" />
        </div>

        <div className='sidebar-scroll'>
          <nav className="menu">
          <button onClick={() => navigate('/agendamento')}>Buscar monitorias</button>
          <button onClick={() => navigate('/sessao-aluno')}>Minhas sessões</button>
        </nav>
        </div>
        
        <button className="logout" onClick={() => navigate('/login')}>
          Sair
        </button>
      </aside>

      <main className="content">
        <header className="topbar">
          <div className="notificacao">
            <img
              src="https://img.icons8.com/ios/50/appointment-reminders--v1.png"
              alt="Notificações"
            />
            <span className="badge" />
          </div>

          <div className="perfil" onClick={() => navigate('/perfil')}>
            <img
              src="https://img.icons8.com/ios-filled/50/user-male-circle.png"
              alt="Perfil"
            />
            <div className="infos">
              <span>Nome Aluno</span>
              <strong>Aluno</strong>
            </div>
          </div>
        </header>

        <section className="boasvindas">
          <h1>Olá, Nome Aluno</h1>
          <p>Acesso rápido às funcionalidades</p>

          <div className="cards">
            <div className="card" onClick={() => navigate('/agendamento')}>
              <h3>Buscar</h3>
              <span>monitorias</span>
            </div>
            <div className="card" onClick={() => navigate('/sessao-aluno')}>
              <h3>Minhas</h3>
              <span>sessões</span>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
};

export default HomeAluno;
