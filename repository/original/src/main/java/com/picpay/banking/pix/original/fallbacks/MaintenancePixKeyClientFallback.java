package com.picpay.banking.pix.original.fallbacks;

import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import com.picpay.banking.pix.original.dto.request.CreateAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.RemoveAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.UpdateAccessKeyAccountDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyAccountUpdateDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyCreateDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyRemoveDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;

public class MaintenancePixKeyClientFallback extends ClientFallback implements MaintenancePixKeyClient {

    public MaintenancePixKeyClientFallback(Throwable cause) {
        super(cause);
    }

    @Override
    public ResponseWrapperDTO<AccessKeyCreateDTO> createPixKey(String requestIdentifier, CreateAccessKeyDTO createAccessKeyDTO) {
        throw resolveException();
    }

    @Override
    public ResponseWrapperDTO<AccessKeyCreateDTO> createEvpPixKey(String requestIdentifier, CreateAccessKeyDTO createAccessKeyDTO) {
        throw resolveException();
    }

    @Override
    public ResponseWrapperDTO<AccessKeyAccountUpdateDTO> update(final String requestIdentifier, final UpdateAccessKeyAccountDTO updateAccessKeyAccountDTO) {
        throw resolveException();
    }

    @Override
    public ResponseWrapperDTO<AccessKeyRemoveDTO> remove(String requestIdentifier, RemoveAccessKeyDTO removeAccessKeyDTO) {
        throw resolveException();
    }

}
