package com.picpay.banking.pix.original.fallbacks;

import com.picpay.banking.pix.original.clients.ClaimClient;
import com.picpay.banking.pix.original.dto.request.CreateClaimRequestDTO;

public class ClaimClientFallback extends ClientFallback implements ClaimClient {

    public ClaimClientFallback(Throwable cause) {
        super(cause);
    }

    @Override
    public Object create(String requestIdentifier, CreateClaimRequestDTO createClaimRequestDTO) {
        throw resolveException();
    }

}
