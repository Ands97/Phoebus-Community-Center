# Community Center Management API

Uma API RESTful para gerenciamento de centros comunitários, permitindo a troca de recursos, controle de ocupação e consulta de histórico de trocas.

## Tecnologias

- **Java 17**
- **Spring Boot 3.3.4**
- **MongoDB**
- **Docker** (para contêinerização)
- **Swagger** (documentação da API)

## Funcionalidades

- Adicionar um novo centro comunitário.
- Atualizar a ocupação de um centro comunitário.
- Trocar recursos entre centros comunitários.
- Consultar centros comunitários com alta ocupação.
- Obter a média de recursos disponíveis nos centros.
- Consultar o histórico de trocas de um centro específico.

## Configuração do Ambiente

1. **Clone o repositório:**

   ```bash
   git clone git@github.com:Ands97/Phoebus-Community-Center.git
   cd community-center

2. **Configuração do Docker:** Certifique-se de ter o Docker e o Docker Compose instalados. Em seguida, inicie os serviços:

   ```bash
   docker build --progress=plain --no-cache -t community-center:latest .
   docker-compose up

3. **Segunda Opção utilizando o Makefile:**
    ```bash
    make build
    make run
       
    # Para ajuda, utilize:
    make help

## Executando os Testes
```bash
mvn test
```

## Documentação da API
http://localhost:8080/swagger-ui/


## Comentários do Projeto

### Decisões de Design
- **Arquitetura Hexagonal:** Adoção de uma arquitetura hexagonal para melhor organização e desenvolvimento de testes. Essa abordagem permite uma separação clara entre lógica de negócio e detalhes de implementação, promovendo um código mais modular e testável.
- **Uso do Lombok:** A biblioteca Lombok foi utilizada para simplificar a criação de getters e setters, reduzindo o tempo de desenvolvimento e melhorando a legibilidade do código.

### Serviços Implementados

- **`addCommunityCenter`:** Implementação de um CRUD simples com validações necessárias.
  
- **`updateOccupancy`:** CRUD com validações para ocupação enviada e busca do centro comunitário, garantindo integridade nas atualizações.

- **`exchangeResources`:** Implementação de transações com a anotação `@Transactional` para garantir atomicidade nas operações. Utilização de `CompletableFuture` para otimizar buscas e salvar intercâmbios em paralelo, além de validações rigorosas para recursos.

- **`getCentersWithHighOccupancy`:** Busca pelo percentual de ocupação armazenado no banco para facilitar consultas eficientes no MongoDB.

- **`getAverageResources`:** Uso de Streams e `Map` para calcular médias de recursos disponíveis. A busca direta por `findAll` é suficiente para o escopo atual, mas com volume de dados maior, deverá ser refeito utilizando técnicas de paginação ou agregação no MongoDB para melhorar a eficiência e evitar gargalos de desempenho.

- **`getExchangeHistory`:** Função com paginação para listar e buscar registros por data, evitando sobrecargas de dados.
