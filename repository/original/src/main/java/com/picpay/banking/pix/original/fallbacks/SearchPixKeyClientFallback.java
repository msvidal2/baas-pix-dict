package com.picpay.banking.pix.original.fallbacks;

import com.picpay.banking.pix.original.clients.SearchPixKeyClient;
import com.picpay.banking.pix.original.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.pix.original.dto.response.ListPixKeyDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;

import java.util.List;

public class SearchPixKeyClientFallback extends ClientFallback implements SearchPixKeyClient {

    public SearchPixKeyClientFallback(Throwable cause) {
        super(cause);
    }

    @Override
    public ResponseWrapperDTO<List<ListPixKeyDTO>> listPixKey(final String requestIdentifier, final ListPixKeyRequestDTO dto) {
        throw resolveException();
    }

}
