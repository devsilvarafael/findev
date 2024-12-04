# Documentação da API

## **Empresa**
As empresas podem ser criadas, atualizadas, excluídas e recuperadas.

### Endpoints:
- `/api/companies`
  - **GET**: Recuperar todas as empresas. ✅
  - **POST**: Criar uma nova empresa. ✅
- `/api/companies/{companyId}` 
  - **PUT**: Atualizar uma empresa existente. ✅
  - **DELETE**: Excluir uma empresa pelo ID. ✅

---

## **Desenvolvedor**
Os desenvolvedores podem ser criados, atualizados, excluídos e recuperados.

### Endpoints:
- `/api/developers`
  - **GET**: Recuperar todos os desenvolvedores. ✅
  - **POST**: Criar um novo desenvolvedor. ✅
- `/api/developers/{id}` 
  - **GET**: Recuperar um desenvolvedor pelo ID. ✅
  - **PUT**: Atualizar um desenvolvedor pelo ID. ✅
  - **DELETE**: Excluir um desenvolvedor pelo ID. ✅
---

## **Vaga**
As vagas podem ser criadas, atualizadas, excluídas, recuperadas, e os desenvolvedores podem se candidatar ou cancelar candidaturas às vagas.

### Endpoints:
- `/api/jobs`
  - **GET**: Recuperar todas as vagas. ✅
  - **POST**: Criar um novo anúncio de vaga. ✅
- `/api/jobs/{id}` 
  - **GET**: Recuperar uma vaga pelo ID. ✅
  - **PUT**: Atualizar uma vaga pelo ID. ✅
  - **DELETE**: Excluir uma vaga pelo ID. ✅
- `/api/jobs/company/{id}` 
  - **GET**: Recuperar todas as vagas de uma empresa específica. ✅
- `/api/jobs/recruiter/{id}` 
  - **GET**: Recuperar todas as vagas postadas por um recrutador específico. ✅
- `/api/jobs/{jobId}/apply` 
  - **POST**: Candidatar-se a uma vaga. ✅
- `/api/jobs/{jobId}/unapply/{developerId}` 
  - **DELETE**: Cancelar a candidatura a uma vaga. ✅
- `/api/jobs/matching` 
  - **GET**: Recuperar vagas compatíveis para um desenvolvedor específico. ✅

---

## **Candidaturas**
As candidaturas às vagas podem ser gerenciadas, incluindo recuperação e atualização de status.

### Endpoints:
- `/api/candidature/developer/{developerId}`  
  - **GET**: Recuperar todas as candidaturas de um desenvolvedor. ✅
- `/api/candidature/job/{jobId}`  
  - **GET**: Recuperar todas as candidaturas de uma vaga específica. ✅
- `/api/candidature/{candidatureId}/status`
  - **PUT**: Atualizar o status de uma candidatura. ✅

---

## **Recrutador**
Os recrutadores podem ser criados, atualizados, excluídos e recuperados.

### Endpoints:
- `/api/recruiters`
  - **GET**: Recuperar todos os recrutadores. ✅
  - **POST**: Criar um novo recrutador. ✅
- `/api/recruiters/{recruiterId}` 
  - **PUT**: Atualizar um recrutador pelo ID. ✅
  - **DELETE**: Excluir um recrutador pelo ID. ✅
- `/api/recruiters/company/{companyId}` 
  - **GET**: Recuperar todos os recrutadores associados a uma empresa específica. ✅

---

## **Autenticação**
Os usuários podem fazer login e recuperar detalhes sobre os usuários.

### Endpoints:
- `/api/auth/users` 
  - **GET**: Recuperar todos os usuários. ✅
- `/api/auth`
  - **POST**: Autenticar um usuário com e-mail e senha. ✅
