package com.picpay.banking.jdpi.fallbacks;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.ListPendingClaimRequestDTO;
import com.picpay.banking.jdpi.dto.request.ClaimCancelRequestDTO;
import com.picpay.banking.jdpi.dto.request.ClaimConfirmationRequestDTO;
import com.picpay.banking.jdpi.dto.request.CompleteClaimRequestDTO;
import com.picpay.banking.jdpi.dto.request.CreateClaimRequestDTO;
import com.picpay.banking.jdpi.dto.request.ListClaimRequestDTO;
import com.picpay.banking.jdpi.dto.response.ClaimCancelResponseDTO;
import com.picpay.banking.jdpi.dto.response.ClaimResponseDTO;
import com.picpay.banking.jdpi.dto.response.FindClaimResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListClaimResponseDTO;

public class ClaimJDClientFallback extends JDClientFallback implements ClaimJDClient {

    public ClaimJDClientFallback(Throwable cause) {
        super(cause);
    }

    @Override
    public ClaimResponseDTO createClaim(String requestIdentifier, CreateClaimRequestDTO dto) {
        throw resolveException();
    }

    @Override
    public ClaimResponseDTO confirmation(String requestIdentifier, String claimId, ClaimConfirmationRequestDTO dto) {
        throw resolveException();
    }

    @Override
    public ClaimCancelResponseDTO cancel(String requestIdentifier, String claimId, ClaimCancelRequestDTO dto) {
        throw resolveException();
    }

    @Override
    public ListClaimResponseDTO listPending(final String requestIdentifier, final ListPendingClaimRequestDTO dto) {
        throw resolveException();
    }


    @Override
    public ClaimResponseDTO complete(String requestIdentifier, String claimId, CompleteClaimRequestDTO dto) {
        throw resolveException();
    }

    @Override
    public ListClaimResponseDTO list(final String requestIdentifier, final ListClaimRequestDTO dto) {
        throw resolveException();
    }

    @Override
    public FindClaimResponseDTO find(String idReivindicacao) {
        throw resolveException();
    }

}
