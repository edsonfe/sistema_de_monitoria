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
import SessaoMonitor from '../pages/SessaoMonitor';
import SessaoDetalheAluno from '../pages/SessaoDetalheAluno';
import AvaliacaoSessao from '../pages/AvaliacaoSessao';
import Chat from '../pages/Chat';
import MateriaisApoio from '../pages/MateriaisApoio';
import CadastrarMonitoria from '../pages/CadastrarMonitoria';
import SessoesCadastradas from '../pages/SessaoMonitor';
import SessaoDetalheMonitor from '../pages/SessaoDetalheMonitor';
import MateriaisApoioMonitor from '../pages/MateriaisApoioMonitor';

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
      <Route path='/sessao-detalhe' element={<SessaoDetalheAluno />} />
      < Route path='/avaliacao' element={<AvaliacaoSessao/>} />
      < Route path='/chat' element={<Chat/>}/>
      < Route path='/materiais' element={<MateriaisApoio/>}/>
      < Route path='/sessao-monitor' element={<SessaoMonitor/>}/>
      < Route path='/cadastrar' element={<CadastrarMonitoria/>}/>
      < Route path='/cadastradas' element={<SessoesCadastradas/>}/>
      < Route path='/sessao-detalhe-monitor' element={<SessaoDetalheMonitor/>}/>
      <Route path="/editar-monitoria" element={<CadastrarMonitoria />} />
      <Route path="/material-apoio" element={<MateriaisApoioMonitor />} />

      

    </Routes>
    
  );
}
