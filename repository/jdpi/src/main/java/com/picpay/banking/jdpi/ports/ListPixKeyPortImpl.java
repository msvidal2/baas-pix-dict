package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.ListPixKeyConverter;
import com.picpay.banking.jdpi.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.ListPixKeyPort;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class ListPixKeyPortImpl implements ListPixKeyPort {

    private PixKeyJDClient pixKeyJDClient;

    private ListPixKeyConverter converter;

    @Override
    public Collection<PixKey> listPixKey(final String requestIdentifier, final PixKey pixKey) {

        var listPixKeyRequestDTO = ListPixKeyRequestDTO.builder()
            .ispb(pixKey.getIspb())
            .nrAgencia(pixKey.getBranchNumber())
            .tpConta(pixKey.getAccountType().getValue())
            .nrConta(pixKey.getAccountNumber())
            .tpPessoa(pixKey.getPersonType().getValue())
            .cpfCnpj(Long.valueOf(pixKey.getTaxId())).build();

        var findPixKeyResponseDTO = pixKeyJDClient.listPixKey(requestIdentifier, listPixKeyRequestDTO);

        return converter.convert(findPixKeyResponseDTO);
    }

}
