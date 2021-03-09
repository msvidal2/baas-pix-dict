/*
 *  baas-pix-dict 1.0 12/9/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.config;

import com.picpay.banking.reconciliation.dto.response.EntryByCidResponse;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.jaxb.JAXBEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * @author rafael.braga
 * @version 1.0 09/12/2020
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
        try {
            return new JAXBContextFactory.Builder()
                .withMarshallerJAXBEncoding("UTF-8")
                //            .withProperty("javax.xml.bind.context.factory", "com.sun.xml.bind.v2.JAXBContextFactory")
                //            .withProperty("javax.xml.bind.JAXBContextFactory", "com.sun.xml.bind.v2.JAXBContextFactory")
                .build(List.of(EntryByCidResponse.class));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}

