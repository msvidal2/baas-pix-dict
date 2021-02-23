package com.picpay.banking.pix.core.ports.pixkey.picpay;


import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;

import java.util.List;
import java.util.Optional;

public interface FindPixKeyPort {

    Optional<PixKey> findDonatedPixKey(String pixKey);

    Optional<PixKey> findPixKey(String pixKey);

    List<PixKey> findByAccount(Integer ispb, String branch, String accountNumber, AccountType accountType);

    Pagination<PixKey> findAllByKeyType(KeyType keyType, Integer page, Integer size);

    Optional<PixKey> findByCid(String cid);

    String computeVsync(KeyType key);

}
