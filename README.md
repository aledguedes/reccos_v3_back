# reccos_v3_back
Reccos V3 Back
Reccos V3 Back é uma API RESTful desenvolvida em Spring Boot para gerenciar um sistema SaaS. O projeto inclui autenticação baseada em JWT, integração com banco de dados (H2 para desenvolvimento), e documentação de API via Swagger.
Tecnologias Utilizadas

Java 21
Spring Boot 3.4.5
Spring Security (autenticação JWT)
Spring Data JPA (com H2 para desenvolvimento)
Flyway (migrações de banco de dados)
MapStruct 1.6.0 (mapeamento de DTOs)
Springdoc OpenAPI (Swagger) 2.5.0
Lombok 1.18.36 (redução de boilerplate)
Maven (gerenciamento de dependências)

Estrutura do Projeto

Entidades: Inclui uma entidade para administradores do sistema.
DTOs: Utiliza record para imutabilidade nos objetos de transferência de dados.
Controllers: Endpoints para criação de administradores e autenticação.
Serviços: Contém a lógica de negócios para criação e autenticação de administradores.
Configurações: 
Configuração de segurança com JWT.
Configuração do Swagger para documentação e testes interativos.


Migrações: Flyway para criação de tabelas e inserção de dados iniciais.

Pré-requisitos

Java 21 instalado.
Maven instalado.
Um cliente HTTP (como Postman ou cURL) para testar os endpoints.
Opcionalmente, um navegador para acessar o Swagger UI.

Como Configurar e Rodar
1. Clonar o Repositório
git clone <URL_DO_REPOSITORIO>
cd reccos-v3-back

2. Compilar o Projeto
Use o Maven para compilar o projeto e baixar as dependências:
./mvnw clean install

3. Rodar o Projeto
Execute o projeto no perfil dev para habilitar o banco H2 e o Swagger:
./mvnw spring-boot:run -Dspring.profiles.active=dev

O servidor será iniciado em http://localhost:8080.
4. Acessar Recursos

Swagger UI: Documentação da API e testes interativos.Acesse: http://localhost:8080/swagger-ui/index.html
H2 Console: Banco de dados em memória para desenvolvimento.Acesse: http://localhost:8080/h2-console  
JDBC URL: jdbc:h2:mem:reccosdb  
Usuário: sa  
Senha: (em branco)



Endpoints Disponíveis
A API possui endpoints para:

Criar administradores do sistema (público, com limite de 2 administradores).
Autenticar administradores e obter um token JWT (público).
Testar um endpoint protegido (requer autenticação via JWT).

Use o Swagger UI para explorar os endpoints e suas especificações.
Testando com Swagger

Acesse o Swagger UI: http://localhost:8080/swagger-ui/index.html.
Use o endpoint de autenticação para obter um token JWT.
Clique no botão "Authorize" no topo da página.
Insira o token no formato Bearer <token>.
Teste os endpoints protegidos.

Próximos Passos

Criar uma entidade para usuários e suas associações com federações.
Implementar endpoints para gerenciar usuários e federações.
Configurar permissões baseadas em papéis (ex.: OWNER, ADMIN, MANAGER, USER).

Contribuindo

Faça um fork do projeto.
Crie uma branch para sua feature (git checkout -b feature/nova-feature).
Commit suas alterações (git commit -m 'Adiciona nova feature').
Envie para o repositório remoto (git push origin feature/nova-feature).
Abra um Pull Request.

Licença
Este projeto está licenciado sob a licença MIT.
