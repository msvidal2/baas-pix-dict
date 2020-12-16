#language:pt
Funcionalidade: Verificar Sincronismo após operações de CRUD
  Esta funcionalidade tem o objetivo de modificar as chaves do pix e garantir que a base local esteja sincronizada com a base remota do Bacen.
  Como contexto, vamos assumir que a base local tenha todas as chaves e todos os tipos estão sincronizados (OK).

  Cenário: A base de dados local deve estar completa e ok com o bacen
    Dado que a base de dados esteja completa

  Esquema do Cenário: Sincronismo de base após operações de CRUD
    Dado que exista uma chave do pix do tipo "<keyType>"
    Quando uma operação de "<operation>" é executada
#    Então um evento do tipo "<type>" deve existir no banco de dados
    E a sincronização entre as bases deve estar OK

    Exemplos:
      | keyType   | operation | type         |
      | CPF       | CREATE    | ADD          |
#      | CPF       | UPDATE    | ADD + REMOVE |
#      | CPF       | REMOVE    | REMOVE       |
#      | CNPJ      | CREATE    | ADD          |
#      | CNPJ      | UPDATE    | ADD + REMOVE |
#      | CNPJ      | REMOVE    | REMOVE       |
#      | CELLPHONE | CREATE    | ADD          |
#      | CELLPHONE | UPDATE    | ADD + REMOVE |
#      | CELLPHONE | REMOVE    | REMOVE       |
#      | EMAIL     | CREATE    | ADD          |
#      | EMAIL     | UPDATE    | ADD + REMOVE |
#      | EMAIL     | REMOVE    | REMOVE       |
#      | RANDOM    | CREATE    | ADD          |
#      | RANDOM    | UPDATE    | ADD + REMOVE |
#      | RANDOM    | REMOVE    | REMOVE       |

#  Cenário: Após todas alterações tudo deve permanecer OK
#    Dado a sincronização de base seja executada
#    Então os 5 tipos de chaves devem estar sincronizados