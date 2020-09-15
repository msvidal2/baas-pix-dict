package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.ListClaimPort;
import com.picpay.banking.pix.core.ports.ListPendingClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ListClaimUseCase {

    ListPendingClaimPort listPendingClaimPort;

    ListClaimPort listClaimPort;

    private DictItemValidator<Claim> validator;

    public ClaimIterable execute(final Claim claim, final Boolean isPending, final Integer limit, final String requestIdentifier){

        validator.validate(claim);

        ClaimIterable claimIterable = null;

        if(isPending) {
            claimIterable = listPendingClaimPort.list(claim, limit, requestIdentifier);
        } else {
            claimIterable = listClaimPort.list(claim, limit, requestIdentifier);
        }

        return claimIterable;
    }
}
