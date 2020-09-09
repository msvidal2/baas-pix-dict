package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.converter.ListAddressingKeyConverter;
import com.picpay.banking.jdpi.dto.request.ListAddressingKeyRequestDTO;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.ports.ListAddressingKeyPort;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class ListAddressingKeyPortImpl implements ListAddressingKeyPort {

    private AddressingKeyJDClient addressingKeyJDClient;

    private ListAddressingKeyConverter converter;

    @Override
    public Collection<AddressingKey> listAddressingKey(final String requestIdentifier, final AddressingKey addressingKey) {

        var listAddressingKeyRequestDTO = ListAddressingKeyRequestDTO.builder()
            .ispb(addressingKey.getIspb())
            .nrAgencia(addressingKey.getBranchNumber())
            .tpConta(addressingKey.getAccountType().getValue())
            .nrConta(addressingKey.getAccountNumber())
            .tpPessoa(addressingKey.getPersonType().getValue())
            .cpfCnpj(Long.valueOf(addressingKey.getCpfCnpj())).build();

        var findAddressingKeyResponseDTO = addressingKeyJDClient.listAddressingKey(requestIdentifier, listAddressingKeyRequestDTO);

        return converter.convert(findAddressingKeyResponseDTO);
    }

}
