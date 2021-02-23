#BAAS-PIX-DICT-V2 API

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=com.picpay.banking.pix.dict%3Abaas-pix-dict&metric=bugs&token=49dc3d89f9cdb079be93552c17dcc9d768d0dd7a)](https://sonarcloud.io/dashboard?id=com.picpay.banking.pix.dict%3Abaas-pix-dict)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.picpay.banking.pix.dict%3Abaas-pix-dict&metric=code_smells&token=49dc3d89f9cdb079be93552c17dcc9d768d0dd7a)](https://sonarcloud.io/dashboard?id=com.picpay.banking.pix.dict%3Abaas-pix-dict)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.picpay.banking.pix.dict%3Abaas-pix-dict&metric=coverage&token=49dc3d89f9cdb079be93552c17dcc9d768d0dd7a)](https://sonarcloud.io/dashboard?id=com.picpay.banking.pix.dict%3Abaas-pix-dict)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.picpay.banking.pix.dict%3Abaas-pix-dict&metric=duplicated_lines_density&token=49dc3d89f9cdb079be93552c17dcc9d768d0dd7a)](https://sonarcloud.io/dashboard?id=com.picpay.banking.pix.dict%3Abaas-pix-dict)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.picpay.banking.pix.dict%3Abaas-pix-dict&metric=ncloc&token=49dc3d89f9cdb079be93552c17dcc9d768d0dd7a)](https://sonarcloud.io/dashboard?id=com.picpay.banking.pix.dict%3Abaas-pix-dict)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.picpay.banking.pix.dict%3Abaas-pix-dict&metric=sqale_rating&token=49dc3d89f9cdb079be93552c17dcc9d768d0dd7a)](https://sonarcloud.io/dashboard?id=com.picpay.banking.pix.dict%3Abaas-pix-dict)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.picpay.banking.pix.dict%3Abaas-pix-dict&metric=alert_status&token=49dc3d89f9cdb079be93552c17dcc9d768d0dd7a)](https://sonarcloud.io/dashboard?id=com.picpay.banking.pix.dict%3Abaas-pix-dict)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.picpay.banking.pix.dict%3Abaas-pix-dict&metric=reliability_rating&token=49dc3d89f9cdb079be93552c17dcc9d768d0dd7a)](https://sonarcloud.io/dashboard?id=com.picpay.banking.pix.dict%3Abaas-pix-dict)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.picpay.banking.pix.dict%3Abaas-pix-dict&metric=security_rating&token=49dc3d89f9cdb079be93552c17dcc9d768d0dd7a)](https://sonarcloud.io/dashboard?id=com.picpay.banking.pix.dict%3Abaas-pix-dict)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.picpay.banking.pix.dict%3Abaas-pix-dict&metric=sqale_index&token=49dc3d89f9cdb079be93552c17dcc9d768d0dd7a)](https://sonarcloud.io/dashboard?id=com.picpay.banking.pix.dict%3Abaas-pix-dict)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=com.picpay.banking.pix.dict%3Abaas-pix-dict&metric=vulnerabilities&token=49dc3d89f9cdb079be93552c17dcc9d768d0dd7a)](https://sonarcloud.io/dashboard?id=com.picpay.banking.pix.dict%3Abaas-pix-dict)

---
### Propósito:
O propósito desse projeto visa a integração do produto PIX do PicPay e de outros clientes com os serviços do PIX Bacen, 
tendo como objetivo o uso dessa API como BAAS (Banking as a Service).

---

### Gostaria de Mais Informações Sobre o Projeto?
[Leia nossa documentação aqui](https://picpay.atlassian.net/wiki/spaces/SPI/pages/1412695071/DICT)

---
### O que é o DICT BACEN?
O Diretório de Identificadores de Contas Transacionais (DICT) é o serviço do arranjo Pix no BACEN que permite buscar detalhes 
de contas transacionais com chaves de endereçamento mais convenientes para quem faz um pagamento. Entre os tipos de 
chave atualmente disponíveis estão CPF, CNPJ, telefone, e-mail e EVP (Chaves Random). As informações retornadas pelo DICT permitem ao 
pagador confirmar a identidade do recebedor, proporcionando uma experiência mais fácil e segura. 
Permitem também ao PSP do pagador criar a mensagem de instrução de pagamento a ser enviada para o sistema de liquidação 
com os detalhes de conta do recebedor. [fonte: api bacen 1.2.0](https://www.bcb.gov.br/content/estabilidadefinanceira/pix/API%20do%20DICT-v1.2.0.html)