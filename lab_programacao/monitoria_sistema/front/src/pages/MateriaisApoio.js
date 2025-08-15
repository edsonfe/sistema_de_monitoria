import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import '../styles/MateriaisApoio.css';

export default function MateriaisApoio() {
  const navigate = useNavigate();
  const { sessaoId } = useLocation().state || {};

  const [materiais, setMateriais] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState(null);
  const [modalAberto, setModalAberto] = useState(false);
  const [materialAtual, setMaterialAtual] = useState(null);

  // Carrega materiais da sessão
  useEffect(() => {
    if (!sessaoId) return navigate(-1);

    const carregarMateriais = async () => {
      try {
        setLoading(true);
        const res = await fetch(`http://localhost:8080/api/materiais/sessao/${sessaoId}`);
        if (!res.ok) throw new Error('Erro ao carregar materiais');
        const dados = await res.json();
        setMateriais(dados);
        setErro(null);
      } catch (e) {
        setErro(e.message);
      } finally {
        setLoading(false);
      }
    };

    carregarMateriais();
  }, [sessaoId, navigate]);

  // Abre modal para visualizar material
  const abrirMaterial = (mat) => {
    setMaterialAtual(mat);
    setModalAberto(true);
  };

  // Baixa arquivo ou abre link
  const baixarMaterial = (mat) => {
    if (mat.arquivoUrl) {
      const link = document.createElement('a');
      link.href = mat.arquivoUrl;
      link.download = mat.arquivo;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } else if (mat.link) {
      window.open(mat.arquivoUrl, '_blank');
    } else {
      alert('Material não possui arquivo ou link para download.');
    }
  };

  if (loading) return <p>Carregando materiais...</p>;
  if (erro) return <p style={{ color: 'red' }}>Erro: {erro}</p>;
  if (materiais.length === 0) return <p>Nenhum material disponível para esta sessão.</p>;

  return (
    <div className="content">
      {/* Voltar */}
      <div className="voltar-home" onClick={() => navigate(-1)} style={{ cursor: 'pointer' }}>
        <img
          src="https://img.icons8.com/ios-filled/24/03bcd3/left.png"
          alt="Voltar"
        />
        <span style={{ marginLeft: '3px' }}>Voltar</span>
      </div>

      <h2>Materiais de Apoio</h2>

      <div className="lista-materiais">
        {materiais.map((mat) => (
          <div className="material" key={mat.materialId}>
            <div className="info">
              <h3>{mat.titulo}</h3>
              <p>
                {mat.arquivo ? 'Arquivo' : ''} {mat.link ? 'Link' : ''} • {new Date(mat.dataSessao).toLocaleDateString()}
              </p>
            </div>
            <div className="acoes">
              <button className="baixar" onClick={() => baixarMaterial(mat)}>
                Baixar
              </button>
              <button className="abrir" onClick={() => abrirMaterial(mat)}>
                Abrir
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Modal de visualização */}
      {modalAberto && materialAtual && (
        <div className="modal-material" onClick={() => setModalAberto(false)}>
          <div
            className="modal-material-conteudo"
            onClick={(e) => e.stopPropagation()}
          >
            <h3>{materialAtual.titulo}</h3>
            {materialAtual.arquivoUrl && (
              <p>
                📎 <a href={materialAtual.arquivoUrl} target="_blank" rel="noreferrer">{materialAtual.arquivo}</a>
              </p>
            )}
            {materialAtual.link && (
              <p>
                🔗 <a href={materialAtual.link} target="_blank" rel="noreferrer">{materialAtual.link}</a>
              </p>
            )}
            <button onClick={() => setModalAberto(false)}>Fechar</button>
          </div>
        </div>
      )}
    </div>
  );
}
