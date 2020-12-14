/*
 *  baas-pix-dict 1.0 12/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.domain.infraction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rafael.braga
 * @version 1.0 11/12/2020
 */
@Getter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ListInfractionReports {

    private List<InfractionReport> infractionReports;
    private LocalDateTime responseTime;
    private boolean hasMoreElements;
    private int size;

}
