package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.bacen.AcknowledgeClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendClaimNotificationPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class PollingClaimListenerUseCase {

    private Integer participant;
    private AcknowledgeClaimPort acknowledgeClaimPort;
    private CreateClaimPort saveClaimPort;
    private SendClaimNotificationPort sendClaimNotificationPort;

    public void execute(Claim claim) {
        if (claim.isOpen(participant)) {
            claim = acknowledgeClaimPort.acknowledge(
                    claim.getClaimId(),
                    claim.getDonorIspb());
        }
        saveClaimPort.saveClaim(claim, null);

        sendClaimNotificationPort.send(claim);

        log.info("claim_notification_sent_to_topic", kv("claimId", claim.getClaimId()));
    }

}
