/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pixkey.entity;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pixkey.dto.request.AccountType;
import com.picpay.banking.pixkey.dto.request.KeyType;
import com.picpay.banking.pixkey.dto.request.OwnerType;
import com.picpay.banking.pixkey.dto.request.Reason;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author rafael.braga
 * @version 1.0 16/11/2020
 */
@Entity(name = "pix_key")
@NoArgsConstructor
@Data
public class PixKeyEntity {

    @Id
    private String key;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime updateDate;
    private KeyType type;

    //Account
    private String participant;
    private String branch;
    private String accountNumber;
    private AccountType accountType;

    // Owner
    private OwnerType ownerType;
    private String taxId;
    private String name;

    //reason
    private Reason reason;

    public PixKey toPixKey() {
        return null;
    }

}
