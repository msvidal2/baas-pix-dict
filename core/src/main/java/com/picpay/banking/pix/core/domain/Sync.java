package com.picpay.banking.pix.core.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    }

    public void verify(final List<String> cidsInBacen, final List<PixKey> contentIdentifiers) {
        final var cidsInDatabase = contentIdentifiers.stream()
            .map(PixKey::getCid)
            .collect(Collectors.toList());


        this.cidsSyncronized =  cidsInDatabase.stream()
            .filter(cidsInBacen::contains)
            .collect(Collectors.toList());
        log.info("Verifying Keys syncronized with Bacen- cids size {}", cidsSyncronized.size());

        final var cidsNotSyncronizedWithDatabaseAndNeedRemove = cidsInDatabase.stream()
            .filter(cid -> !cidsInBacen.contains(cid))
            .collect(Collectors.toList());

        final var cidsNotSyncronizedAtDatabaseAndNeedToInsert = cidsInBacen.stream()
            .filter(cid -> !cidsInDatabase.contains(cid))
            .collect(Collectors.toList());

        this.cidsNotSyncronized =  Stream.concat(cidsNotSyncronizedAtDatabaseAndNeedToInsert.stream(),cidsNotSyncronizedWithDatabaseAndNeedRemove.stream())
            .collect(Collectors.toList());

        log.info("Verifying Keys not syncronized with Bacen - cids size {}", this.cidsNotSyncronized.size());

    }

}
