package com.picpay.banking.pix.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.picpay.banking.pix.adapters.incoming.web"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private static ApiInfo apiInfo() {
        return new ApiInfo(
                "BAAS - DICT API",
                "Diretório de Identificadores de Contas Transacionais. <br>\n" +
                        "    O DICT é o componente do arranjo que será instituído pelo BC que armazenará as informações das chaves ou apelidos que servem para identificar as contas transacionais dos usuários recebedores de maneira intuitiva e simplificada, permitindo que o usuário pagador utilize informações que ele já possui sobre o usuário recebedor para iniciar o pagamento.\n" +
                        "    <br> <br>",
                "0.0.2",
                null,
                null,
                null, null, Collections.emptyList());
    }

}

