package com.picpay.banking.pix.processor.claim;

import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.ClaimEventData;
import com.picpay.banking.pix.core.usecase.claim.CreateClaimUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.picpay.banking.pix.core.events.Domain.CLAIM;
import static com.picpay.banking.pix.core.events.EventType.CLAIM_CREATED_BACEN;

@RequiredArgsConstructor
@Component(value = "createClaimBacenProcessor")
public class CreateClaimBacenProcessor implements EventProcessor<ClaimEventData> {

    private final CreateClaimUseCase createClaimUseCase;

    @Override
    public DomainEvent<ClaimEventData> process(DomainEvent<ClaimEventData> domainEvent) {
        var eventData = domainEvent.getSource();

        var createdClaim = createClaimUseCase.execute(eventData.toClaim(), domainEvent.getRequestIdentifier());

        return DomainEvent.<ClaimEventData>builder()
                .eventType(CLAIM_CREATED_BACEN)
                .domain(CLAIM)
                .source(ClaimEventData.from(createdClaim))
                .requestIdentifier(domainEvent.getRequestIdentifier())
                .build();
    }

    @Override
    public EventType failedEventType() {
        return EventType.CLAIM_FAILED_BACEN;
    }

}
