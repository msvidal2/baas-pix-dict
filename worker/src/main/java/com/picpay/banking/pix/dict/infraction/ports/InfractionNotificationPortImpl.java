package com.picpay.banking.pix.dict.infraction.ports;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionNotificationPort;
import org.springframework.stereotype.Component;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 08/12/20
 */
@Component
public class InfractionNotificationPortImpl implements InfractionNotificationPort {
    @Override
    public void notify(InfractionReport infractionReport) {

    }
}
