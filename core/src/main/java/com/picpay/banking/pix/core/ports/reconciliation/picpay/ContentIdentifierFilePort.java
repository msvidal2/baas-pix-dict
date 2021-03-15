package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;

public interface ContentIdentifierFilePort {

    void saveFile(ContentIdentifierFile contentIdentifierFile);

}
