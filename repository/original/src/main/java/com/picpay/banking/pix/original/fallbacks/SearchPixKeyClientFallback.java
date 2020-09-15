package com.picpay.banking.pix.original.fallbacks;

import com.picpay.banking.pix.original.clients.SearchPixKeyClient;
import com.picpay.banking.pix.original.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.pix.original.dto.response.ListPixKeyResponseDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;

public class SearchPixKeyClientFallback extends ClientFallback implements SearchPixKeyClient {

    public SearchPixKeyClientFallback(Throwable cause) {
        super(cause);
    }

    @Override
    public ResponseWrapperDTO<ListPixKeyResponseDTO> listPixKey(final ListPixKeyRequestDTO dto) {
        return null;
    }

}
