/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pixkey.entity;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pixkey.dto.request.Account;
import com.picpay.banking.pixkey.dto.request.AccountType;
import com.picpay.banking.pixkey.dto.request.KeyType;
import com.picpay.banking.pixkey.dto.request.Owner;
import com.picpay.banking.pixkey.dto.request.Reason;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author rafael.braga
 * @version 1.0 16/11/2020
 */
@Table(name = "pix_key")
@Data
public class PixKeyEntity {

    @Id
    private String key;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime updateDate;
    private KeyType keyType;
    //Account?
    private final String participant;
    private final String branch;
    private final String accountNumber;
    private final AccountType accountType;
    private Account brazilianAccount;
    private Owner owner;
    //reason
    private Reason reason;

    public PixKey toPixKey() {
        return null;
    }

}
