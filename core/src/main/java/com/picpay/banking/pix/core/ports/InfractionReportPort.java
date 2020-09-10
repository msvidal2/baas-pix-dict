package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.Infraction;

public interface InfractionReportPort {

    Infraction create(Infraction infraction, String requestIdentifier);

}
