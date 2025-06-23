import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/MateriaisApoio.css';

export default function MateriaisApoio() {
  const navigate = useNavigate();
  const [modalAberto, setModalAberto] = useState(false);

  const materiais = [
    {
      id: 1,
      titulo: 'Material A',
      tipo: 'PDF',
      tamanho: '1.5MB',
      data: '12/03/2025'
    },
    {
      id: 2,
      titulo: 'Material B',
      tipo: 'Form',
      tamanho: '2.4MB',
      data: '12/03/2025'
    }
  ];

  return (
    <div className="content">
      <div className="voltar-home" onClick={() => navigate(-1)}>
        <img
          src="https://img.icons8.com/ios-filled/24/03bcd3/left.png"
          alt="Voltar"
        />
        <span style={{ marginLeft: '3px' }}>Voltar</span>
      </div>

      <h2>Materiais de Apoio</h2>

      <div className="lista-materiais">
        {materiais.map((mat) => (
          <div className="material" key={mat.id}>
            <div className="info">
              <h3>{mat.titulo}</h3>
              <p>
                Tipo: {mat.tipo} • Tamanho: {mat.tamanho} • Data: {mat.data}
              </p>
            </div>
            <div className="acoes">
              <button className="baixar" onClick={() => alert('Iniciando download...')}>
                Baixar
              </button>
              <button className="abrir" onClick={() => setModalAberto(true)}>
                Abrir
              </button>
            </div>
          </div>
        ))}
      </div>

      {modalAberto && (
        <div className="modal-material" onClick={() => setModalAberto(false)}>
          <div
            className="modal-material-conteudo"
            onClick={(e) => e.stopPropagation()}
          >
            <h3>Visualização do Material</h3>
            <p>Aqui você poderia visualizar o conteúdo do arquivo ou link selecionado.</p>
            <button onClick={() => setModalAberto(false)}>Fechar</button>
          </div>
        </div>
      )}
    </div>
  );
}
