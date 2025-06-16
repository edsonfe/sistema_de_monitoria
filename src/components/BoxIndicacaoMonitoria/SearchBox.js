export default function SearchBox({ value, onChange }) {
  return (
    <div className="search-box">
      <input
        type="text"
        placeholder="Buscar monitoria"
        value={value}
        onChange={onChange}
      />
      <span className="search-icon">🔍</span>
    </div>
  );
}
