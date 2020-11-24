package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.Vsync;

import java.util.Optional;

public interface DatabaseVsyncPort {

    Vsync getLastSuccessfulVsync(KeyType keyType);

}
