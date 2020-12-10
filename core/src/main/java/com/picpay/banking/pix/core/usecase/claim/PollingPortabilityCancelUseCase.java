package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PollingPortabilityCancelUseCase {

    private final FindClaimToCancelPort findClaimToCancelPort;

    public List<Claim> execute(ClaimType type, List<ClaimSituation> status, Integer donorParticipant, Integer limit) {

        //Verificar no banco de dados local a cada 1 minuto as reivindicações com o status diferente
        //de CANCELED ou COMPLETED em que o PicPay é o doador, e o ResolutionPeriodEnd é igual ou menor a data atual.
        List<Claim> claimsToCancel = findClaimToCancelPort.find(ClaimType.PORTABILITY, ClaimSituation.getPending(), donorParticipant,
                LocalDateTime.now(), limit);

        log.debug("Claims to cancel found: {}", claimsToCancel.size());

        return claimsToCancel;
    }

}