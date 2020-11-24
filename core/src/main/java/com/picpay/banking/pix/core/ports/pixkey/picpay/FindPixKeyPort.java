package com.picpay.banking.pix.core.ports.pixkey.picpay;


import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PixKey;

import java.util.List;

public interface FindPixKeyPort {

    PixKey findPixKey(String pixKey);

    List<PixKey> findByAccount(Integer ispb, String branch, String accountNumber, AccountType accountType);

}
