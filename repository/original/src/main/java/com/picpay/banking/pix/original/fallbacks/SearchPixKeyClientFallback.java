package com.picpay.banking.pix.original.fallbacks;

import com.picpay.banking.pix.original.clients.SearchPixKeyClient;

public class SearchPixKeyClientFallback extends ClientFallback implements SearchPixKeyClient {

    public SearchPixKeyClientFallback(Throwable cause) {
        super(cause);
    }

}
