import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Sessao.css";

export default function SessaoAluno() {
  const navigate = useNavigate();
  const [sessoes, setSessoes] = useState([]);
  const [loading, setLoading] = useState(true);

  const alunoId = localStorage.getItem("usuarioId");

  useEffect(() => {
    if (!alunoId) {
      alert("Usuário não autenticado!");
      navigate("/login");
      return;
    }

    // Busca sessões do aluno
    fetch(`http://localhost:8080/api/sessoes/aluno/${alunoId}`)
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao carregar sessões");
        return res.json();
      })
      .then((data) => {
        // Filtra somente sessões deferidas
        const deferidas = data.filter((s) => s.status === "DEFERIDA");

        // Ordena por data
        deferidas.sort((a, b) => new Date(a.data) - new Date(b.data));

        setSessoes(deferidas);
      })
      .catch((err) => {
        console.error(err);
        alert("Falha ao carregar sessões.");
      })
      .finally(() => setLoading(false));
  }, [alunoId, navigate]);

  return (
    <div className="content">
      <div className="voltar-home" onClick={() => navigate("/home-aluno")}>
        <img
          src="https://img.icons8.com/ios-filled/24/03bcd3/left.png"
          alt="Voltar"
        />
        <span>Voltar</span>
      </div>

      <h2>Minhas Sessões Deferidas</h2>

      {loading ? (
        <p>Carregando sessões...</p>
      ) : sessoes.length === 0 ? (
        <p className="sem-sessoes">Nenhuma sessão deferida encontrada.</p>
      ) : (
        <div className="lista-sessoes">
          {sessoes.map((sessao) => {
            const dataHora = new Date(sessao.data);
            const dataFormatada = dataHora.toLocaleDateString("pt-BR", {
              day: "2-digit",
              month: "2-digit",
              year: "numeric",
            });
            const horaFormatada = dataHora.toLocaleTimeString("pt-BR", {
              hour: "2-digit",
              minute: "2-digit",
            });

            return (
              <div className="card-sessao" key={sessao.sessaoId}>
                <h3>{sessao.disciplinaMonitoria}</h3>
                <p>
                  <strong>Data:</strong> {dataFormatada} às {horaFormatada}
                </p>
                <p>
                  <strong>Monitor:</strong> {sessao.alunoNome || "Monitor"}
                </p>
                {sessao.linkSalaVirtual && (
                  <a
                    className="btn-entrar"
                    href={sessao.linkSalaVirtual}
                    target="_blank"
                    rel="noopener noreferrer"
                  >
                    Entrar na Sala Virtual
                  </a>
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
