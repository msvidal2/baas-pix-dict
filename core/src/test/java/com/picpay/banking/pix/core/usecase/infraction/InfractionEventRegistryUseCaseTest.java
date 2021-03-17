package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.events.InfractionReportEvent;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.events.data.InfractionAnalyzeEventData;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionEventRegistryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.picpay.banking.pix.core.events.InfractionReportEvent.CREATE_PENDING;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InfractionEventRegistryUseCaseTest {

    @InjectMocks
    private InfractionEventRegistryUseCase useCase;

    @Mock
    private InfractionEventRegistryPort infractionEventRegistryPort;

    private InfractionReportEventData eventData;

    @BeforeEach
    void setup() {
        eventData = InfractionReportEventData.builder()
                .infractionReportId(randomUUID().toString())
                .ispb(224123)
                .analyze(InfractionAnalyzeEventData.builder()
                        .analyzeResult(InfractionAnalyzeResult.ACCEPTED)
                        .details("dsaasdas")
                        .build())
                .build();
    }

    @Test
    void when_executeWithSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> useCase.execute(CREATE_PENDING, randomUUID().toString(), eventData));

        verify(infractionEventRegistryPort).registry(any(InfractionReportEvent.class), anyString(), any(InfractionReportEventData.class));
    }

    @Test
    void when_executeWithNullEvent_expect_nullPointerException() {
        assertThrows(NullPointerException.class,
                () -> useCase.execute(null, randomUUID().toString(), eventData));
    }

    @Test
    void when_executeWithNullRequestIdentifier_expect_nullPointerException() {
        assertThrows(NullPointerException.class,
                () -> useCase.execute(CREATE_PENDING, null, eventData));
    }

    @Test
    void when_executeWithNullInfractionReportDataEvent_expect_nullPointerException() {
        assertThrows(NullPointerException.class,
                () -> useCase.execute(CREATE_PENDING, randomUUID().toString(), null));
    }

}