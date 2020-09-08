package com.picpay.banking.jdpi.fallbacks;

import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.dto.request.CreateAddressingKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.ListAddressingKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.RemoveAddressingKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.UpdateAccountAddressingKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreateAddressingKeyResponseJDDTO;
import com.picpay.banking.jdpi.dto.response.FindAddressingKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListAddressingKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.RemoveAddressingKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.UpdateAccountAddressingKeyResponseDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressingKeyJDClientFallback extends JDClientFallback implements AddressingKeyJDClient {

    public AddressingKeyJDClientFallback(Throwable cause) {
        super(cause);
    }

    @Override
    public CreateAddressingKeyResponseJDDTO createAddressingKey(String requestIdentifier, CreateAddressingKeyRequestDTO dto) {
        throw resolveException();
    }

    @Override
    public RemoveAddressingKeyResponseDTO removeKey(String requestIdentifier, String key, RemoveAddressingKeyRequestDTO dto) {
        throw resolveException();
    }

    @Override
    public FindAddressingKeyResponseDTO findAddressingKey(final String key, final String userId, final String PIEndToEndId, final String PIPayerId) {
        throw resolveException();
    }

    @Override
    public ListAddressingKeyResponseDTO listAddressingKey(final String requestIdentifier, final ListAddressingKeyRequestDTO dto) {
        throw resolveException();
    }

    @Override
    public UpdateAccountAddressingKeyResponseDTO updateAccount(String requestIdentifier,
                                                               String key,
                                                               UpdateAccountAddressingKeyRequestDTO dto) {
        throw resolveException();
    }

}
