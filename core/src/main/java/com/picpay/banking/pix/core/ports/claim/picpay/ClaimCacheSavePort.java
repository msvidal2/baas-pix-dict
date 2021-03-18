package com.picpay.banking.pix.core.ports.claim.picpay;

import com.picpay.banking.pix.core.events.data.ClaimEventData;
import lombok.NonNull;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 12/03/21
 */
public interface ClaimCacheSavePort {

    void save(@NonNull ClaimEventData claim, @NonNull String requestIdentifier);

}
