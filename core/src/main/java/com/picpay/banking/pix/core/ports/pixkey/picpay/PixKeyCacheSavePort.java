package com.picpay.banking.pix.core.ports.pixkey.picpay;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import lombok.NonNull;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 12/03/21
 */
public interface PixKeyCacheSavePort {

    void save(@NonNull PixKey pixKey, @NonNull String requestIdentifier);

}
