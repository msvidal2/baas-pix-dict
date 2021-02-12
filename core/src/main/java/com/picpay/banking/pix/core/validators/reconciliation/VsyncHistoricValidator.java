package com.picpay.banking.pix.core.validators.reconciliation;

import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VsyncHistoricValidator {

    public static void validate(final SyncVerifierHistoric syncVerifierHistoric) {
        if (syncVerifierHistoric == null) {
            throw new IllegalArgumentException("VsyncHistoric cannot be null");
        }

        if (syncVerifierHistoric.getKeyType() == null) {
            throw new IllegalArgumentException("The attribute KeyType of VsyncHistoric cannot be null");
        }

        if (syncVerifierHistoric.getSynchronizedStart() == null) {
            throw new IllegalArgumentException("The attribute SynchronizedAt of VsyncHistoric cannot be null");
        }
    }

}
