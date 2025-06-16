import { Routes, Route } from 'react-router-dom';
import Login from '../pages/Login';
import HomeAluno from '../pages/HomeAluno';
import HomeMonitor from '../pages/HomeMonitor';
import Cadastro from '../pages/Cadastro';
import MonitoriasDisponiveis from '../pages/MonitoriasDisponiveis';


export default function AppRoutes() {
  return (
    <Routes>
      
      <Route path="/" element={<Login />} />
      <Route path="/login" element={<Login />} />
      <Route path="/home-aluno" element={<HomeAluno />} />
      <Route path="/home-monitor" element={<HomeMonitor />} />
      <Route path="/cadastro" element={<Cadastro />} />
      <Route path="/monitorias" element={<MonitoriasDisponiveis />} />

    </Routes>
    
  );
}
