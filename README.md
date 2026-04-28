# Sistema de Monitoria Acadêmica

## 📌 Descrição

O **Sistema de Monitoria Acadêmica** é uma plataforma que conecta **alunos**, **monitores** e **professores** para facilitar o agendamento de aulas de monitoria, compartilhamento de materiais e feedbacks.

Professores podem gerar **tokens de acesso** para que monitores se cadastrem, garantindo segurança e controle no processo.

---

## Funcionalidades

- Cadastro de aluno, monitor e professor
- Geração de token de acesso para monitores
- Publicação de datas, links e materiais pelo monitor
- Envio de feedbacks e avaliações pelos alunos
- Área para consulta de avaliações
- Controle de permissões por tipo de usuário

---

## Tecnologias Utilizadas

- **Backend:** Java + Spring Boot
- **Frontend:** React
- **Banco de Dados:** Usando Docker como para gerenciar
- **Autenticação:** JWT (JSON Web Token)

---

## Instalação e Configuração

### 1. Clonar repositório

```bash
git clone https://github.com/edsonfe/sistema_de_monitoria.git
cd sistema-monitoria
```

### 2. Configurar backend

- Instale o Java 17+
- Configure o PostgreSQL
- Ajuste o arquivo `application.properties` com suas credenciais

### 3. Rodar o backend

```bash
./mvnw spring-boot:run
```

### 4. Configurar frontend

- Instale o Node.js 18+

```bash
cd frontend
npm install
npm start
```

### 5. Subindo o projeto

1️⃣ Criar volume para o banco
docker volume create db_data

2️⃣ Iniciar os containers
docker compose up --build -d

Recarregar o backup
Se precisar restaurar o banco a partir do monitoria_dump.sql novamente:
docker compose down -v
docker volume create db_data
docker compose up --build -d

### 6. Cadastro inicial

Caso ocorram problemas com restauração do banco é necessário cadastrar pelo menos um curso e uma disciplina via API para que o sistema funcione.

Requisições no Postman
Curso
URL
POST http://localhost:8080/api/cursos

Headers
Content-Type: application/json

Body (raw / JSON)
{
"nome": "Engenharia de Software",
"descricao": "Curso voltado para o desenvolvimento e manutenção de sistemas."
}
Disiciplinas
URL:
http://localhost:8080/api/disciplinas

Headers:
Content-Type: application/json

Body (raw – JSON):
{
"nome": "Estruturas de Dados",
"codigo": "ED001",
"cargaHoraria": 60,
"cursoId": 1
}

---

## Testes

- **Unitários:** JUnit

---

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch com sua feature: `git checkout -b minha-feature`
3. Commit suas alterações: `git commit -m 'Minha nova feature'`
4. Envie para a branch principal: `git push origin minha-feature`
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob a licença MIT. Consulte o arquivo LICENSE para mais detalhes.

---

## 👨‍💻 Autores

- Davi dos Santos
- Edson Carlos
- Lucas Ascençao
