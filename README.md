# Plataforma de Otimização de Rotas e Logística em Tempo Real (Em construção)

## Sobre o Projeto

Este projeto é uma plataforma de backend completa para a otimização e acompanhamento de rotas de entrega, simulando a arquitetura e os desafios de sistemas de logística modernos como iFood e Uber Eats. A aplicação foi desenvolvida com um foco em alta performance, escalabilidade e processamento de eventos em tempo real, utilizando uma arquitetura de microserviços orquestrada com Docker.

O sistema é capaz de receber um conjunto de pedidos, distribuí-los de forma inteligente entre os entregadores disponíveis, calcular a rota mais eficiente para cada um, fornecer acompanhamento da localização em tempo real e permitir buscas complexas nos dados dos pedidos.

---
### Principais Funcionalidades

* **Motor de Otimização de Rotas (VRP):** Utiliza o **Timefold (OptaPlanner)** para resolver o Problema de Roteirização de Veículos, calculando as sequências de entrega mais eficientes para múltiplos pedidos e entregadores.
* **Cálculo de Rotas Geoespaciais:** Integra-se com o **Neo4j** e a biblioteca **Graph Data Science (GDS)** para calcular as rotas mais curtas em uma malha viária real, extraída do OpenStreetMap.
* **Rastreamento em Tempo Real:** Implementa um sistema de eventos com **Apache Kafka** e **Redis (Geoespacial)** que permite o acompanhamento da localização dos entregadores em tempo real.
* **Busca Full-Text:** Integra-se com o **Elasticsearch** para fornecer uma API de busca de texto completo, rápida e relevante para os pedidos, com os dados a serem indexados de forma assíncrona através do Kafka.
* **Arquitetura Orientada a Eventos:** A comunicação entre os diferentes domínios (pedidos, localização, otimização, busca) é feita de forma assíncrona, garantindo que a API se mantenha rápida e resiliente.
* **Gestão de Dados Operacionais:** Uma API RESTful completa, construída com **Java e Spring Boot**, para gerir as entidades de negócio (Pedidos, Entregadores, Veículos) num banco de dados **PostgreSQL/PostGIS**.
* **Automatização de Migrações:** Utiliza o **Flyway** (para PostgreSQL) e o **Neo4j-Migrations** para gerir e aplicar automaticamente o schema e os dados iniciais dos bancos de dados.

---
### Arquitetura

O sistema é composto por múltiplos serviços containerizados que se comunicam através de uma rede Docker interna.

* **API Principal (Spring Boot):** O ponto de entrada que orquestra as operações.
* **PostgreSQL + PostGIS:** A "fonte da verdade" para os dados operacionais (pedidos, entregadores).
* **Neo4j + GDS:** O especialista em grafos, responsável por armazenar o mapa e calcular as distâncias.
* **Redis:** Atua como um cache de alta performance para os cálculos de distância e para armazenar a localização em tempo real dos entregadores e os resultados das otimizações.
* **Apache Kafka (KRaft):** O "sistema nervoso" do projeto, que transmite eventos (como atualizações de localização e de pedidos) de forma assíncrona.
* **Elasticsearch + Kibana:** O motor de busca, que indexa os dados dos pedidos para permitir consultas rápidas e complexas.

---
### Ambiente Técnico e Tecnologias

* **Linguagem & Framework:** Java 21, Spring Boot 3
* **Bancos de Dados:** PostgreSQL (com PostGIS), Neo4j, Redis, Elasticsearch
* **Mensageria e Eventos:** Apache Kafka (em modo KRaft)
* **Otimização e Grafos:** Timefold (OptaPlanner), Neo4j Graph Data Science (GDS)
* **Infraestrutura e DevOps:** Docker, Docker Compose
* **Gestão de Banco de Dados:** Flyway (PostgreSQL), Neo4j-Migrations (Neo4j)
* **APIs e Protocolos:** REST, JSON

---
### Como Executar o Projeto

#### Pré-requisitos

* [Docker](https://www.docker.com/get-started) e Docker Compose
* Java 21 (ou superior)
* Git

#### Passos de Instalação

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/EduardoRez3nde/logi-graph-api
    cd logi-graph-api
    ```

2.  **Prepare os Dados do Mapa:**
    * Garanta que os seus ficheiros de mapa (`vertices.csv` e `edges.csv`) estão na pasta `./import`.

3.  **Inicie o Ambiente Docker:**
    Este comando irá construir a imagem da sua aplicação e iniciar todos os contentores em segundo plano.
    ```bash
    docker compose up --build -d
    ```

4.  **Aguarde a Inicialização:**
    A primeira inicialização pode demorar alguns minutos. Monitore o status dos contentores com:
    ```bash
    docker compose ps
    ```
    Aguarde até que todos os serviços estejam com o status `(healthy)` ou `running`.

5.  **Verifique os Logs (Opcional):**
    Para confirmar que as migrações automáticas para ambos os bancos de dados foram executadas com sucesso, verifique os logs da sua aplicação:
    ```bash
    docker compose logs -f app
    ```
    Você deverá ver mensagens a indicar que as migrações do Flyway e do Neo4j foram aplicadas e que a projeção do GDS foi criada.

O sistema estará totalmente operacional e pronto para receber requisições.

---
### Endpoints Principais da API

#### Otimização de Rotas

* **Iniciar um trabalho de otimização:**
    `POST /routes/optimize`
    ```bash
    curl -X POST -H "Content-Type: application/json" \
    -d '{"orderIds": [1, 2]}' \
    "http://localhost:8080/routes/optimize"
    ```

* **Verificar o status e obter o resultado:**
    `GET /routes/optimize/status/{jobId}`
    ```bash
    curl -X GET "http://localhost:8080/routes/optimize/status/<seu-job-id>"
    ```

#### Rastreamento em Tempo Real

* **Atualizar a localização de um entregador:**
    `POST /drivers/{driverId}/location`
    ```bash
    curl -X POST -H "Content-Type: application/json" \
    -d '{"latitude": -3.115, "longitude": -60.015}' \
    "http://localhost:8080/drivers/1/location"
    ```

* **Acompanhar um pedido:**
    `GET /orders/{orderId}/track`
    ```bash
    curl -X GET "http://localhost:8080/orders/1/track"
    ```

#### Busca de Pedidos

* **Buscar pedidos por texto e com paginação:**
    `GET /search/orders`
    ```bash
    curl -X GET "http://localhost:8080/search/orders?query=teste&page=0&size=5"
    ```

---
### Atualmente implementando:

* **Refinamento e Produção:** testes (unitários e de integração), configuração de um pipeline de CI/CD (Jenkins), refinar a documentação da API e aprimorar os algoritmos de otimização.
