package com.picpay.banking.pix.core.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private Set<String> cidsSyncronized;

    private Set<String> cidsNotSyncronized;

    public Sync(final ContentIdentifierFile contentIdentifierFile) {
        this.contentIdentifierFile = contentIdentifierFile;
        this.cidsSyncronized = new HashSet<>();
        this.cidsNotSyncronized = new HashSet<>();
    }

    public void verify(final List<String> cidsInBacen, final List<PixKey> contentIdentifiers) {
        final var cidsInDatabase = contentIdentifiers.stream()
            .map(PixKey::getCid)
            .collect(Collectors.toList());

        final var listOfSyncronized = cidsInDatabase.stream()
            .filter(cidsInBacen::contains)
            .collect(Collectors.toList());

        this.cidsSyncronized.addAll(listOfSyncronized);
        log.info("Verifying Keys syncronized with Bacen- cids size {}", cidsSyncronized.size());

        final var listOfcidsNotSyncronized = cidsInDatabase.stream()
            .filter(cid -> !cidsInBacen.contains(cid))
            .collect(Collectors.toList());

        final var cidsNotSyncronizedAtDatabaseAndNeedToInsert = cidsInBacen.stream()
            .filter(cid -> !cidsInDatabase.contains(cid))
            .collect(Collectors.toList());

        this.cidsNotSyncronized.addAll(cidsNotSyncronizedAtDatabaseAndNeedToInsert);
        this.cidsNotSyncronized.addAll(listOfcidsNotSyncronized);;

        log.info("Verifying Keys not syncronized with Bacen - cids size {}", this.cidsNotSyncronized.size());

    }

}
