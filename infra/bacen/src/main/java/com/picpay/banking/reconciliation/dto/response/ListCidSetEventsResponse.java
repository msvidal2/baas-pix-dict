package com.picpay.banking.reconciliation.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "ListCidSetEventsResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListCidSetEventsResponse {

    @XmlElement(name = "HasMoreElements")
    private boolean hasMoreElements;

    @XmlElement(name = "Participant")
    private String participant;

    @XmlElement(name = "KeyType")
    private KeyTypeBacen keyType;

    @XmlElement(name = "StartTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime startTime;

    @XmlElement(name = "EndTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime endTime;

    @XmlElement(name = "SyncVerifierStart")
    private String syncVerifierStart;

    @XmlElement(name = "SyncVerifierEnd")
    private String syncVerifierEnd;

    @XmlElement(name = "CidSetEvents")
    private CidSetEvents cidSetEvents;

    public enum Type {
        ADDED,
        REMOVED
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement(name = "CidSetEvents")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CidSetEvents {

        @XmlElement(name = "CidSetEvent")
        private List<CidSetEvent> events;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement(name = "CidSetEvent")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CidSetEvent {

        @XmlElement(name = "Type")
        private Type type;

        @XmlElement(name = "Cid")
        private String cid;

        @XmlElement(name = "Timestamp")
        @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
        private LocalDateTime timestamp;

        public static ReconciliationEvent toContentIdentifierEvent(final ListCidSetEventsResponse.CidSetEvent cidSetEvent) {
            return ReconciliationEvent.builder()
                .cid(cidSetEvent.getCid())
//                .key(??)  TODO: Rever
//                .action(??)   TODO: Rever
                .keyType(KeyType.valueOf(cidSetEvent.getType().name()))
                .eventOnBacenAt(cidSetEvent.getTimestamp())
                .build();
        }
    }
}
