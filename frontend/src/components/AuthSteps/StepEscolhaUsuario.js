import BotaoPrimario from '../Shared/BotaoPrimario';
import BotaoSecundario from '../Shared/BotaoSecundario';
import LinhaComTexto from './LinhaComTexto';

export default function StepEscolhaUsuario({ onEscolher }) {
  return (
    <div className="opcoes log-step active">
      <h2>Área</h2>
      <BotaoPrimario text="Entrar como Aluno" onClick={() => onEscolher('home-aluno')} />
      <BotaoPrimario text="Entrar como Mentor" onClick={() => onEscolher('home-monitor')} />
      <LinhaComTexto>Primeiro acesso?</LinhaComTexto>
      <BotaoSecundario text="Cadastrar-se" to="/cadastro" />
    </div>
  );
}
