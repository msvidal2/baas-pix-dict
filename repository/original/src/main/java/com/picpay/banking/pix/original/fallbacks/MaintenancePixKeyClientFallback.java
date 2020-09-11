package com.picpay.banking.pix.original.fallbacks;

import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import com.picpay.banking.pix.original.dto.request.CreateAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.UpdateAccessKeyAccountDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyAccountUpdateDTO;
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

    @Override
    public ResponseWrapperDTO<AccessKeyAccountUpdateDTO> update(UpdateAccessKeyAccountDTO updateAccessKeyAccountDTO) {
        return null;
    }

}
