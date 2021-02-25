package com.picpay.banking.pix.core.domain.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class ReconciliationFixByCidFile {

    private final KeyType keyType;
    private final ContentIdentifierFile contentIdentifierFile;

    public ReconciliationFixByCidFile(final KeyType keyType, final ContentIdentifierFile contentIdentifierFile) {
        this.keyType = keyType;
        this.contentIdentifierFile = contentIdentifierFile;
    }

    public List<String> computeCidsThatMustBeRemoved(final Collection<String> cidsInDatabase) {
        var cidsInFile = contentIdentifierFile.getContent();

        return cidsInDatabase.parallelStream()
            .filter(cid -> !cidsInFile.contains(cid))
            .collect(Collectors.toList());
    }

    public List<String> computeCidsThatMustBeCreated(final Collection<String> cidsInDatabase) {
        var cidsInFile = contentIdentifierFile.getContent();

        return cidsInFile.parallelStream()
            .filter(cid -> !cidsInDatabase.contains(cid))
            .collect(Collectors.toList());
    }

}
