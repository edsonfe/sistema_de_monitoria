import { Routes, Route } from 'react-router-dom';
import Login from '../pages/Login';
import HomeAluno from '../pages/HomeAluno';
import HomeMonitor from '../pages/HomeMonitor';
import Cadastro from '../pages/Cadastro';
import MonitoriasDisponiveis from '../pages/MonitoriasDisponiveis';
import Perfil from '../pages/Perfil';
import BuscarMonitoria from '../pages/BuscarMonitoria';
import DetalheMonitoria from '../pages/DetalheMonitoria';
import SessaoAluno from '../pages/SessaoAluno';
import SessaoDetalhes from '../pages/SessaoDetalhes';
import AvaliacaoSessao from '../pages/AvaliacaoSessao';


export default function AppRoutes() {
  return (
    <Routes>
      
      <Route path="/" element={<Login />} />
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />
      <Route path="/monitorias" element={<MonitoriasDisponiveis />} />
      <Route path="/home-aluno" element={<HomeAluno />} />
      <Route path="/home-monitor" element={<HomeMonitor />} />
      <Route path='/perfil' element={<Perfil />} />
      <Route path='/buscar-monitoria' element={<BuscarMonitoria/>} />
      <Route path='/detalhe-monitoria/:id' element={<DetalheMonitoria/>} />
      <Route path='/sessao-aluno' element={<SessaoAluno/>} />
      <Route path='/sessao-detalhe' element={<SessaoDetalhes />} />
      < Route path='/avaliacao' element={<AvaliacaoSessao/>} />
      

    </Routes>
    
  );
}
