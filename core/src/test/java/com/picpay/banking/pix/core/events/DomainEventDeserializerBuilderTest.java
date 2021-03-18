package com.picpay.banking.pix.core.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DomainEventDeserializerBuilderTest {

    @Test
    void deserializeInfraction() {
        var json = "{\"eventType\":\"INFRACTION_REPORT_CANCEL_PENDING\",\"domain\":\"INFRACTION_REPORT\",\"source\":{\"infractionReportId\":\"2aefbdfd-8dfd-45cf-8d09-02e6b21cecc0\",\"ispb\":22896431},\"errorEvent\":null,\"requestIdentifier\":\"7d3b10af-e24d-4ceb-994b-4bf12b24c024\"}";

        assertDoesNotThrow(() -> {
            var objectMapper = new ObjectMapper();
            var result = objectMapper.readValue(json, DomainEvent.class);

            assertEquals(InfractionReportEventData.class, result.getSource().getClass());
        });
    }

    @Test
    void deserializePixKey() {
        var json = "{\"eventType\":\"PIX_KEY_CREATE_PENDING\",\"domain\":\"PIX_KEY\",\"source\":{\"type\":\"EMAIL\",\"key\":\"testereivindicacao@picpay.com\",\"ispb\":22896431,\"branchNumber\":\"0001\",\"accountType\":\"CHECKING\",\"accountNumber\":\"0001098013\",\"accountOpeningDate\":\"2020-08-14T13:59:12.646\",\"personType\":\"INDIVIDUAL_PERSON\",\"taxId\":\"93405736099\",\"name\":\"Maria do Socorro\",\"situation\":\"OPEN\",\"reason\":\"CLIENT_REQUEST\"},\"errorEvent\":null,\"requestIdentifier\":\"38d2f32e-8f16-4208-a2c3-718fd035d832\"}";

        assertDoesNotThrow(() -> {
            var objectMapper = new ObjectMapper();
            var result = objectMapper.readValue(json, DomainEvent.class);

            assertEquals(PixKeyEventData.class, result.getSource().getClass());
        });
    }

}