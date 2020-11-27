package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.Vsync;

public interface FailureMessagePort {

    void sendMessageForSincronization(Vsync vsync);

}
