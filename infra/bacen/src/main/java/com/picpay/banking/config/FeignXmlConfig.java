/*
 *  baas-pix-dict 1.0 11/24/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.config;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.jaxb.JAXBEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author rafael.braga
 * @version 1.0 24/11/2020
 */
@Configuration
public class FeignXmlConfig {

    @Bean
    @Primary
    public Encoder encoder(JAXBContextFactory jaxbContextFactory) {
        return new JAXBEncoder(jaxbContextFactory);
    }

    @Bean
    @Primary
    public Decoder decoder(JAXBContextFactory jaxbContextFactory) {
        return new JAXBDecoder(jaxbContextFactory);
    }

    @Bean
    public JAXBContextFactory jaxbContextFactory() {
        return new JAXBContextFactory.Builder()
            .withMarshallerJAXBEncoding("UTF-8")
            .build();
    }

}
