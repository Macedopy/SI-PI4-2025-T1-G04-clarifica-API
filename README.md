# Clarifica Obra API

Projeto designado a disponibilizar uma API robusta para receber dados vindo do UI ou de qualquer outra requisição e tratá-los até chegar no armazenamento do banco de dados.
Este projeto é focado para auxiliar usuários finais a terem o acesso real ao progresso e o gerenciamento de suas construções e projetos, com ele, pode-se ter acesso a toda construção que está sendo gerenciada pela construtora, facilitando o dia a dia do cliente economizando seu tempo para evitar visitas desnecessárias na construção.
## Scripts para início
### `./mvnw quarkus:dev`

Estará disponível a aplicação no link http://localhost:8080.
Poderá realizar requisições com Postman, Insomnia e etc.

Exemplo de requisição:
curl --request POST \
  --url http://localhost:8080/users/customer \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/12.1.0' \
  --data '{
  "id": "12345",
  "personalInformation": {
    "firstName": "Joao",
    "lastName": "Silva",
    "email": "ricardo.silva@example.com",
    "phone": "55-11-98765-4321",
    "cpf": "123.456.789-20"
  },
  "role": "CLIENT"
}'

## Técnico
O projeto está separado em MVC componentes relacionados a cada fase da construção, com camadas de Controller, Service, Repository, cada camada tem sua responsabilidade e facilita a manutenção e desenvolvimento de novas funcionalidades.

## Contribuição
É obrigatório a criação de uma branch local em sua máquina alternativa da main, após as alterações, será feito uma reunião para confirmar as mudanças e realizar testes com os integrantes, após isso está liberado o commit na main.
