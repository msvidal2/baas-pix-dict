package com.picpay.banking.pix.core.ports.pixkey.picpay;

import java.time.LocalDateTime;

public interface RemovePixKeyAutomaticallyPort {

    void remove(String pixKey, LocalDateTime completionThresholdDate);

}
