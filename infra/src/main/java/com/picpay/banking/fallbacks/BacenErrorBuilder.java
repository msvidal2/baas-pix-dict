package com.picpay.banking.fallbacks;

import com.picpay.banking.fallbacks.dto.Problem;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.util.stream.Collectors;

@Slf4j
public class BacenErrorBuilder {

    private FieldResolver fieldResolver;

    private Problem problem;

    private byte[] responseBody;

    public static BacenErrorBuilder builder() {
        return new BacenErrorBuilder();
    }

    public BacenErrorBuilder withFieldResolver(final FieldResolver fieldResolver) {
        this.fieldResolver = fieldResolver;
        return this;
    }

    public BacenErrorBuilder withBody(final byte[] responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public BacenError build() {
        parse();

        if(problem == null) {
            return null;
        }

        var fields = problem.getViolations().stream()
                .map(v -> fieldResolver.resolve(v))
                .collect(Collectors.toList());

        return BacenError.builder()
                .message(problem.getTitle())
                .detail(problem.getDetail())
                .correlationId(problem.getCorrelationId())
                .fields(fields)
                .build();
    }

    private void parse() {
        if(responseBody == null || responseBody.length == 0) {
            return;
        }

        try {
            var context = JAXBContext.newInstance(Problem.class);
            var unmarshaller = context.createUnmarshaller();

            var inputStream = new ByteArrayInputStream(responseBody);

            problem = (Problem) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            log.error("Erro when parse response body", e);
        }
    }
}
