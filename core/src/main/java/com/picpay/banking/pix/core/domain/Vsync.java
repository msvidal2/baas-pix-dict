package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.domain.ContentIdentifier.ContentIdentifierType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class Vsync {

    private String vsync;
    private KeyType keyType;
    private LocalDateTime synchronizedAt;
    private VsyncResult vsyncResult;

    public void calculateVsync(final List<ContentIdentifier> contentIdentifiers) {
        if (vsync == null) vsync = "0000000000000000000000000000000000000000000000000000000000000000";

        BigInteger vsyncAsBigInteger = new BigInteger(vsync, 16);

        for (ContentIdentifier contentIdentifier : contentIdentifiers) {
            BigInteger cidAsBigInteger = new BigInteger(contentIdentifier.getCid(), 16);
            vsyncAsBigInteger = vsyncAsBigInteger.xor(cidAsBigInteger);
        }

        vsync = vsyncAsBigInteger.toString(16);
    }

    public Set<Action> identifyActions(final List<ContentIdentifier> bacenCids,
        final List<ContentIdentifier> databaseCids) {

        var bacenLatestActions = groupByCidMaxByDateAndMapToActions(bacenCids);
        var databaseLatestActions = groupByCidMaxByDateAndMapToActions(databaseCids);

        var resultActions = hasInBacenAndNotHaveInDatabase(bacenLatestActions, databaseLatestActions);

        resultActions.addAll(hasInDatabaseAndNotHaveInBacen(databaseLatestActions, bacenLatestActions));

        return resultActions;
    }

    private Set<Action> hasInDatabaseAndNotHaveInBacen(final Set<Action> databaseLatestActions, final Set<Action> bacenLatestActions) {
        var databaseAddEvents = databaseLatestActions.stream()
            .filter(action -> action.getActionType().equals(ActionType.ADD))
            .collect(Collectors.toSet());

        return CollectionUtils.subtract(databaseAddEvents, bacenLatestActions)
            .stream().map(cid -> new Action(cid.getCid(), ActionType.REMOVE))
            .collect(Collectors.toSet());
    }

    private Set<Action> hasInBacenAndNotHaveInDatabase(final Set<Action> bacenLatestActions, final Set<Action> databaseLatestActions) {
        return new HashSet<>(CollectionUtils.subtract(bacenLatestActions, databaseLatestActions));
    }

    private Set<Action> groupByCidMaxByDateAndMapToActions(final List<ContentIdentifier> bacenCids) {
        return new HashSet<>(bacenCids.stream().collect(
            Collectors.groupingBy(ContentIdentifier::getCid,
                Collectors.maxBy(Comparator.comparing(ContentIdentifier::getDateTime))))
            .values()).stream().map(cid -> new Action(cid.get().getCid(), ActionType.resolve(cid.get().getContentIdentifierType())))
            .collect(Collectors.toSet());
    }

    public void syncVerificationResult(final VsyncResult result) {
        this.vsyncResult = result;
        if (vsyncResult.equals(VsyncResult.OK)) {
            this.synchronizedAt = LocalDateTime.now();
        }
    }

    public boolean isNOk() {
        return vsyncResult.equals(VsyncResult.NOK);
    }

    public enum VsyncResult {
        OK,
        NOK
    }

    public enum ActionType {
        ADD,
        REMOVE;

        public static ActionType resolve(final ContentIdentifierType eventType) {
            if (eventType.equals(ContentIdentifierType.ADD)) return ADD;
            return REMOVE;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Action {

        private String cid;
        private ActionType actionType;

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Action action = (Action) o;
            return Objects.equals(cid, action.cid) &&
                actionType == action.actionType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(cid, actionType);
        }

    }

}
