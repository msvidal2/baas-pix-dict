/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.events;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {

    PIX_KEY_CREATE_PENDING(PixKeyEventData.class),
    PIX_KEY_UPDATE_PENDING(PixKeyEventData.class),
    PIX_KEY_REMOVE_PENDING(PixKeyEventData.class),
    PIX_KEY_CREATED_BACEN(PixKeyEventData.class),
    PIX_KEY_REMOVED_BACEN(PixKeyEventData.class),
    PIX_KEY_UPDATED_BACEN(PixKeyEventData.class),
    PIX_KEY_FAILED_BACEN(PixKeyEventData.class),

    CLAIM_CREATE_PENDING(Claim.class),
    CLAIM_CANCEL_PENDING(Claim.class),
    CLAIM_CONFIRM_PENDING(Claim.class),
    CLAIM_COMPLETE_PENDING(Claim.class),
    CLAIM_CREATED_BACEN(Claim.class),
    CLAIM_CANCELED_BACEN(Claim.class),
    CLAIM_CONFIRMED_BACEN(Claim.class),
    CLAIM_COMPLETED_BACEN(Claim.class),
    CLAIM_FAILED_BACEN(Claim.class),

    INFRACTION_REPORT_CREATE_PENDING(InfractionReportEventData.class),
    INFRACTION_REPORT_ANALYZE_PENDING(InfractionReportEventData.class),
    INFRACTION_REPORT_CANCEL_PENDING(InfractionReportEventData.class),
    INFRACTION_REPORT_CREATED_BACEN(InfractionReportEventData.class),
    INFRACTION_REPORT_ANALYZING_BACEN(InfractionReportEventData.class),
    INFRACTION_REPORT_CANCELED_BACEN(InfractionReportEventData.class),
    INFRACTION_REPORT_FAILED_BACEN(InfractionReportEventData.class);

    private Class aClass;

}
