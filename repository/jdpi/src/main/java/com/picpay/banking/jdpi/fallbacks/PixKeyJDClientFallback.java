package com.picpay.banking.jdpi.fallbacks;

import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.request.CreatePixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.RemovePixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.UpdateAccountPixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreatePixKeyResponseJDDTO;
import com.picpay.banking.jdpi.dto.response.FindPixKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListPixKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.RemovePixKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.UpdateAccountPixKeyResponseDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PixKeyJDClientFallback extends JDClientFallback implements PixKeyJDClient {

    public PixKeyJDClientFallback(Throwable cause) {
        super(cause);
    }

    @Override
    public CreatePixKeyResponseJDDTO createPixKey(String requestIdentifier, CreatePixKeyRequestDTO dto) { throw resolveException();
    }

    @Override
    public RemovePixKeyResponseDTO removeKey(String requestIdentifier, String key, RemovePixKeyRequestDTO dto) {
        throw resolveException();
    }

    @Override
    public FindPixKeyResponseDTO findPixKey(final String key, final String userId, final String PIEndToEndId, final String PIPayerId) {
        throw resolveException();
    }

    @Override
    public ListPixKeyResponseDTO listPixKey(final String requestIdentifier, final ListPixKeyRequestDTO dto) {
        throw resolveException();
    }

    @Override
    public UpdateAccountPixKeyResponseDTO updateAccount(String requestIdentifier,
                                                        String key,
                                                        UpdateAccountPixKeyRequestDTO dto) {
        throw resolveException();
    }

}
