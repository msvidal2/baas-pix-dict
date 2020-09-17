package com.picpay.banking.pix.original.fallbacks;

import com.picpay.banking.pix.original.clients.ClaimClient;
import com.picpay.banking.pix.original.dto.request.CreateClaimRequestDTO;
import com.picpay.banking.pix.original.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;

public class ClaimClientFallback extends ClientFallback implements ClaimClient {

    public ClaimClientFallback(Throwable cause) {
        super(cause);
    }

    @Override
    public ResponseWrapperDTO<ClaimResponseDTO> create(String requestIdentifier, CreateClaimRequestDTO createClaimRequestDTO) {
        throw resolveException();
    }

}
