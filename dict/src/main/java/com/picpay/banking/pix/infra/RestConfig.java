package com.picpay.banking.pix.infra;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

@Configuration
public class RestConfig {

	@Bean
	public RestTemplate restTemplate(ClientHttpRequestInterceptor jdpiTokenInterceptor) {
		var httpClient = HttpClientBuilder.create().build();
		var clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

		var restTemplate = new RestTemplate(clientHttpRequestFactory);

		restTemplate.getInterceptors().add(jdpiTokenInterceptor);

		return restTemplate;
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return builder -> {
			builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			builder.serializers(new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
			builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		};
	}

}
