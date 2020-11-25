package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class Vsync {

    private String vsync;
    private KeyType keyType;
    private LocalDateTime synchronizedAt;
    private VsyncResult vsyncResult;

    public List<Action> identifyActions(final List<ContentIdentifierEvent> contentIdentifierEvents) {
        return contentIdentifierEvents.stream().collect(
            Collectors.groupingBy(ContentIdentifierEvent::getCid,
                Collectors.maxBy(Comparator.comparing(ContentIdentifierEvent::getDateTime))))
            .values().stream().map(event -> new Action(event.get().getCid(), ActionType.resolve(event.get().getEventType())))
            .collect(Collectors.toList());
    }

    public void calculateVsync(final List<ContentIdentifier> contentIdentifiers) {
        if (vsync == null) vsync = "0000000000000000000000000000000000000000000000000000000000000000";

        BigInteger vsyncAsBigInteger = new BigInteger(vsync, 16);

        for (ContentIdentifier contentIdentifier : contentIdentifiers) {
            BigInteger cidAsBigInteger = new BigInteger(contentIdentifier.getCid(), 16);
            vsyncAsBigInteger = vsyncAsBigInteger.xor(cidAsBigInteger);
        }

        vsync = vsyncAsBigInteger.toString(16);
    }

    public void syncVerificationResult(final VsyncResult result) {
        this.vsyncResult = result;
        if (vsyncResult.equals(VsyncResult.OK)) {
            this.synchronizedAt = LocalDateTime.now();
        }
    }

    public enum VsyncResult {
        OK,
        NOK
    }

    public enum ActionType {
        ADD,
        REMOVE;

        public static ActionType resolve(final EventType eventType) {
            if (eventType.equals(EventType.ADD)) return ADD;
            return REMOVE;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Action {

        private String cid;
        private ActionType actionType;

    }

}
