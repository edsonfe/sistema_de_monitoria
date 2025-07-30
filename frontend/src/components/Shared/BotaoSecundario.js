// BotaoSecundario.jsx
export default function BotaoSecundario({ text, to }) {
  return (
    <a href={to} className="btn2">
      {text}
    </a>
  );
}
