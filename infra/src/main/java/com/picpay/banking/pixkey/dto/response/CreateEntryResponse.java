/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pixkey.dto.response;

import com.picpay.banking.pixkey.dto.request.Entry;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author rafael.braga
 * @version 1.0 16/11/2020
 */
@Getter
@Builder
@AllArgsConstructor
public class CreateEntryResponse {

    private final Entry entry;

    public PixKeyEntity toEntity() {
        return null;
    }

}
