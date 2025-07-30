// BotaoPrimario.jsx
export default function BotaoPrimario({ text, onClick }) {
  return (
    <a href="#" className="btn" onClick={(e) => { e.preventDefault(); onClick(); }}>
      {text}
    </a>
  );
}
