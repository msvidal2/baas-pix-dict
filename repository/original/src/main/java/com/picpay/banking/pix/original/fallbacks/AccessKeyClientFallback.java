package com.picpay.banking.pix.original.fallbacks;

import com.picpay.banking.pix.original.clients.AccessKeyClient;
import com.picpay.banking.pix.original.dto.request.CreateAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.RemoveAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.UpdateAccessKeyAccountDTO;
import com.picpay.banking.pix.original.dto.response.*;

import java.util.List;

public class AccessKeyClientFallback extends ClientFallback implements AccessKeyClient {

    public AccessKeyClientFallback(Throwable cause) {
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

    @Override
    public ResponseWrapperDTO<FindPixKeyResponseDTO> findByKey(String requestIdentifier, String key, String responsableKey) {
        throw resolveException();
    }

    @Override
    public ResponseWrapperDTO<List<ListPixKeyResponseDTO>> listPixKey(String requestIdentifier, String taxId) {
        throw resolveException();
    }

}
