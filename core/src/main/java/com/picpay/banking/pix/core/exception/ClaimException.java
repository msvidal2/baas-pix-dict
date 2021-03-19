package com.picpay.banking.pix.core.exception;

import lombok.Getter;

@Getter
public class ClaimException extends UseCaseException {

    private ClaimError claimError;

    public ClaimException(ClaimError claimError) {
        this.claimError = claimError;
    }

    public ClaimException(String message, ClaimError claimError) {
        super(message);
        this.claimError = claimError;
    }

    public ClaimException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return claimError.getCode();
    }

    @Override
    public String getMessage() {
        return claimError.getMessage();
    }

}
