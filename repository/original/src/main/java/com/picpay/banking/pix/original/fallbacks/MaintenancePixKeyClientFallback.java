package com.picpay.banking.pix.original.fallbacks;

import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import com.picpay.banking.pix.original.dto.request.CreateAccessKeyDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyCreateDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;

public class MaintenancePixKeyClientFallback extends ClientFallback implements MaintenancePixKeyClient {

    public MaintenancePixKeyClientFallback(Throwable cause) {
        super(cause);
    }

    @Override
    public ResponseWrapperDTO<AccessKeyCreateDTO> create(final String requestIdentifier, final CreateAccessKeyDTO createAccessKeyDTO) {
        return null;
    }

}
