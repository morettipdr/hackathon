# Serviço Hackathon (Quarkus)

Serviço REST baseado em Quarkus com Hibernate ORM, JDBC e integrações com Azure (Event Hubs). Executa localmente com conveniências de desenvolvimento e em produção com um banco de dados externo.

- Framework: Quarkus
- Build: Maven Wrapper
- Empacotamento: Fast-jar e imagem Docker
- Banco: H2 para dev; SQL Server para prod (configurável)
- Saúde: `/q/health`, `/q/metrics` (se habilitado)
- Documentação dos endpoints e DTOs: Swagger UI `/q/swagger-ui`

## Requisitos

- Java 21+
- Docker e Docker Compose

## Estrutura do projeto

- `src/main/java` — código da aplicação
- `src/main/resources/application.properties` — configuração
- `Dockerfile` — definição da imagem do container
- `docker-compose.yml` — orquestração local
- `/docs` — Postman Collection

## Executando localmente (modo dev)

Hot-reload, Dev UI e banco em memória por padrão.

```bash
# iniciar
./mvnw quarkus:dev

# Dev UI
http://localhost:8080/q/dev
```

## Empacotamento

```bash
# fast-jar (padrão)
./mvnw clean package

# executar localmente
java -jar target/quarkus-app/quarkus-run.jar
```

## Configuração

Use variáveis de ambiente ou `application.properties`. Variáveis comuns:

```properties
# H2 em memória
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:default;DB_CLOSE_DELAY=-1
quarkus.hibernate-orm.log.sql=true

# SQL Server externo (exemplo)
%prod.quarkus.datasource.product.db-kind=mssql
%prod.quarkus.datasource.product.jdbc.url=${PRODUCT_JDBC_URL}
%prod.quarkus.datasource.product.username=${PRODUCT_DB_USERNAME}
%prod.quarkus.datasource.product.password=${PRODUCT_DB_PASSWORD}
%prod.quarkus.hibernate-orm."product".datasource=product

# Log de acesso
quarkus.http.access-log.enabled=true
quarkus.http.access-log.pattern=combined
```

### Construir e executar com Docker

```bash
# empacotar o app
./mvnw package -DskipTests

# construir imagem (observe o ponto final)
docker build -t quarkus/hackathon -f Dockerfile .

# executar com arquivo de env e mapeamento de porta
docker run --rm -p 8080:8080 --env-file .env quarkus/hackathon
```

## Docker Compose

O Compose constrói a imagem e executa o container com suas variáveis de ambiente.

Comandos:

```bash
# construir e subir
docker compose up

# parar
docker compose down
```

## Exemplo de `.env`

Observe as aspas para URLs JDBC que contêm `;`.

`.env`
```properties
QUARKUS_PROFILE=prod
PRODUCT_JDBC_URL='jdbc:sqlserver://<server>.database.windows.net:1433;databaseName=<db>;encrypt=true;TrustServerCertificate=false;loginTimeout=30'
PRODUCT_DB_USERNAME='<user>'
PRODUCT_DB_PASSWORD='<password>'

# Opcional: Azure Event Hubs
EVENTHUB_CONNECTION_STRING='<connection-string>'
EVENTHUB_NAME='simulacoes'
```

## Saúde, métricas e logs

- Métricas (se habilitado): `GET /q/metrics`

## Segurança (futuro: validação de token OIDC)

Adicione OIDC para validar tokens bearer (Keycloak, Azure AD, etc.):

- Adicionar extensão:
```bash
./mvnw quarkus:add-extension -Dextensions="oidc,smallrye-jwt"
```

- Configurar:
```properties
# Exemplo Azure AD
%prod.quarkus.oidc.auth-server-url=https://login.microsoftonline.com/<tenant-id>/v2.0
%prod.quarkus.oidc.client-id=<client-id>
%prod.quarkus.oidc.credentials.secret=<client-secret>
%prod.quarkus.oidc.application-type=service
%prod.quarkus.oidc.token.audience=api://<audience>

# Proteger todos os endpoints por padrão
quarkus.http.auth.permission.authenticated.paths=/*
quarkus.http.auth.permission.authenticated.policy=authenticated
```

- Proteger endpoints usando JAX-RS e anotações:
```java
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/secure")
public class SecureResource {
  @GET
  @RolesAllowed({"user","admin"})
  public String ping() { return "ok"; }
}
```

## Testes

- Unitários e REST com JUnit 5 e REST Assured.
- Integração com Testcontainers (container MSSQL) para imitar produção.
- Testes de contrato para APIs conforme necessário.

## Observabilidade

- Endpoints para observabilidade já implementados.
- Utilização de micrometer para métricas.
- Para o futuro, é possível configurar um exportador Prometheus ou integração com sistemas de monitoramento.

## CI/CD

- Construa, teste e publique imagens Docker via GitHub Actions.
- Use builds Docker multi-stage e tags imutáveis.
- Faça scan de vulnerabilidades nas imagens.