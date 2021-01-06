#language:pt
Funcionalidade: Verificar Sincronismo após operações de CRUD
  Esta funcionalidade tem o objetivo de modificar as chaves do pix e garantir que a base local esteja sincronizada com a base remota do Bacen.
  Como contexto, vamos assumir que a base local tenha todas as chaves e todos os tipos estão sincronizados (OK).

  Cenário: A base de dados local deve estar completa e ok com o bacen
    Dado que a base de dados esteja completa

  Esquema do Cenário: Sincronismo de base após operações de CRUD
    Dado que exista uma chave do pix do tipo "<keyType>"
    Quando uma operação de "<operation>" é executada
    E a sincronização entre as bases deve estar OK

    Exemplos:
      | keyType   | operation |
      | CPF       | CREATE    |
      | CPF       | UPDATE    |
      | CPF       | REMOVE    |
      | CNPJ      | CREATE    |
      | CNPJ      | UPDATE    |
      | CNPJ      | REMOVE    |
      | CELLPHONE | CREATE    |
      | CELLPHONE | UPDATE    |
      | CELLPHONE | REMOVE    |
      | EMAIL     | CREATE    |
      | EMAIL     | UPDATE    |
      | EMAIL     | REMOVE    |
      | RANDOM    | CREATE    |
#      | RANDOM    | UPDATE    |
      | RANDOM    | REMOVE    |