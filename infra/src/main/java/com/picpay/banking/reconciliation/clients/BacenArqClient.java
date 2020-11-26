/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.reconciliation.clients;

import feign.RequestLine;

import java.net.URI;

public interface BacenArqClient {

    @RequestLine("GET")
    String request(URI baseUri);

}
