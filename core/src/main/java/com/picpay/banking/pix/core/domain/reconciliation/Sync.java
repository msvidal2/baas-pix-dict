package com.picpay.banking.pix.core.domain.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
@Getter
@Slf4j
public class Sync {

    private final ContentIdentifierFile contentIdentifierFile;

    private List<String> cidsSyncronized;

    private List<String> cidsNotSyncronized;

    public Sync(final ContentIdentifierFile contentIdentifierFile) {
        this.contentIdentifierFile = contentIdentifierFile;
        this.cidsSyncronized = new ArrayList<>();
        this.cidsNotSyncronized = new ArrayList<>();
    }

    public void verify(final List<String> cidsInBacen, final List<String> cidsInDatabase) {
        final var listOfSyncronized = cidsInDatabase.parallelStream()
            .filter(cidsInBacen::contains)
            .collect(Collectors.toList());

        this.cidsSyncronized.addAll(listOfSyncronized);
        log.info("ReconciliationSyncByFile_Verifying Keys {} syncronized with Bacen - cids size {}", contentIdentifierFile.getKeyType(),
            cidsSyncronized.size());

        final var cidsInBacenWeNeedInsert = cidsInBacen.parallelStream()
            .filter(cid -> !cidsInDatabase.contains(cid))
            .collect(Collectors.toList());

        final var listOfcidsInDatabaseWeNeedRemoveButCanBeUpdatedWithCorrectCIDInBacen = cidsInDatabase.parallelStream()
            .filter(cid -> !cidsInBacen.contains(cid))
            .collect(Collectors.toList());

        this.cidsNotSyncronized.addAll(cidsInBacenWeNeedInsert);
        this.cidsNotSyncronized.addAll(listOfcidsInDatabaseWeNeedRemoveButCanBeUpdatedWithCorrectCIDInBacen);

        log.info("ReconciliationSyncByFile_Verifying Keys {} not syncronized with Bacen - cids size {}", contentIdentifierFile.getKeyType(),
            this.cidsNotSyncronized.size());
    }

}
