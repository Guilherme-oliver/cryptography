# Desafio Criptografia

Este projeto implementa um serviço de criptografia em uma API, garantindo que os campos sensíveis dos objetos de entidade não sejam visíveis diretamente.
A criptografia é realizada em tempo de execução durante a conversão da entidade para a coluna correspondente no banco de dados, e vice-versa.

## Exemplo

Os campos `userDocument` e `creditCardToken` são considerados sensíveis e devem ser criptografados. A tabela de exemplo seria a seguinte:

| userDocument | creditCardToken | value |
|--------------|-----------------|-------|
| MzYxNDA3ODE4MzM= | YWJjMTIz | 5999 |
| MzI5NDU0MTA1ODM= | eHl6NDU2 | 1000 |
| NzYwNzc0NTIzODY= | Nzg5eHB0bw== | 1500 |

## Requisitos

- Implementar um CRUD simples considerando os campos mencionados acima como sensíveis.
- Utilizar o algoritmo de criptografia de sua preferência. Sugestões: SHA-512 ou PBKDF2.

## Configuração do Projeto

### Docker

O projeto utiliza Docker para rodar o PostgreSQL e o PgAdmin. O arquivo `docker-compose.yml` está configurado da seguinte forma:

```yaml
version: "3.7"
services:
  # ====================================================================================================================
  # POSTGRES SERVER
  # ====================================================================================================================
  pg-docker:
    image: postgres:14-alpine
    container_name: dev-postgresql
    environment:
      POSTGRES_DB: crypt
      POSTGRES_PASSWORD: 123456
    ports:
      - 5433:5432
    volumes:
      - ./.data/postgresql/data:/var/lib/postgresql/data
    networks:
      - dev-network
  # ====================================================================================================================
  # PGADMIN
  # ====================================================================================================================
  pgadmin-docker:
    image: dpage/pgadmin4
    container_name: dev-pgadmin
    environment:
      PGADMIN_DEFAULT_EMAIL: me@example.com
      PGADMIN_DEFAULT_PASSWORD: 123456
    ports:
      - 5050:80
    volumes:
      - ./.data/pgadmin:/var/lib/pgadmin
    depends_on:
      - pg-docker
    networks:
      - dev-network
# ======================================================================================================================
# REDE
# ======================================================================================================================
networks:
  dev-network:
    driver: bridge
```

Observação:

Você pode substituir o PgAdmin por outra ferramenta de sua preferência, como DBeaver.

Configuração do Spring Boot:

spring.datasource.url=jdbc:postgresql://localhost:5433/crypt

spring.datasource.username=postgres

spring.datasource.password=123456

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

spring.jpa.hibernate.ddl-auto=update

Observação:

O projeto possui um arquivo application.properties configurado da seguinte forma:

spring.profiles.active=dev
spring.jpa.open-in-view=false

Dessa forma, ele está executando o application-dev.properties, que é mais adequado para o desenvolvimento em um ambiente de trabalho. Você pode configurá-lo para a execução de testes no modo local, se preferir. 
O projeto também inclui o banco de dados H2; para utilizá-lo, altere "spring.profiles.active=dev" para "spring.profiles.active=test" em application.properties e teste o projeto no H2 localmente.

Documentação:

O projeto também contém a ferramenta Swagger para documentação. Para mais infromações acesse https://swagger.io/

Executando o Projeto

1. Clone o repositório:

SSH:

git clone git@github.com:Guilherme-oliver/cryptography.git

HTTPS:

git clone https://github.com/Guilherme-oliver/cryptography.git

2. Abra e importe a aplicação no seu programa de preferência (STS, Intellij, VS Code...)

3. Inicie os containers Docker:
Pode exacutar tanto no arquivo docker-compose.yml ou
Excutar pelo termina com o comando "docker-compose up -d"

4. Execute a aplicação

5. Contribuição:
Sinta-se à vontade para contribuir com o projeto através de pull requests. Para grandes mudanças, abra uma issue primeiro para discutir o que você gostaria de mudar.
