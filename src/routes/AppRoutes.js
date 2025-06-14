import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from '../pages/A_Login';
import LoginForm from '../pages/B_LoginForm';
import Cadastro from '../pages/B_Cadastro';
import HomeAluno from '../pages/D_HomeAluno';
import HomeMonitor from '../pages/D_HomeMonitor';
import Agendamento from '../pages/C_Agendamento';


const AppRoutes = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/home-aluno" element={<HomeAluno />} />
      <Route path="/home-monitor" element={<HomeMonitor />} />
      <Route path="/login" element={<LoginForm />} />
      <Route path="/cadastro" element={<Cadastro />} />
      <Route path="/agendamento" element={<Agendamento />} />
    </Routes>
  </BrowserRouter>
);

export default AppRoutes;
