#language:pt
Funcionalidade: Verificar Sincronismo
  Esta funcionalidade tem o objetivo de ir até o Bacen e verificar se a base de dados local é idêntica a base de dados do Bacen.

  Esquema do Cenário: Sincronismo de base
    Dado que a base de dados local esteja "<condition>"
    Quando acontece o sincronismo de uma chave do tipo "<keyType>"
    Então o resultado esperado é "<result>"

    Exemplos:
      | condition | keyType   | result |
      | vazia     | CPF       | NOK    |
      | completa  | CPF       | OK     |
      | vazia     | CNPJ      | NOK    |
      | completa  | CNPJ      | OK     |
      | vazia     | CELLPHONE | NOK    |
      | completa  | CELLPHONE | OK     |
      | vazia     | EMAIL     | NOK    |
      | completa  | EMAIL     | OK     |
      | vazia     | RANDOM    | NOK    |
      | completa  | RANDOM    | OK     |