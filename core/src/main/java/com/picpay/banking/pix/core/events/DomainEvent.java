/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.picpay.banking.pix.core.events.data.ErrorEventData;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
@JsonDeserialize(builder = DomainEventDeserializerBuilder.class)
public class DomainEvent<T> {

    private EventType eventType;
    private Domain domain;
    private T source;
    private ErrorEventData errorEvent;
    private String requestIdentifier;

}
