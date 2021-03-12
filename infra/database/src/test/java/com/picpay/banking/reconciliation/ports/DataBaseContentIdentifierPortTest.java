package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.ContentIdentifierFileAction;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.PixKeySituation;
import com.picpay.banking.reconciliation.entity.ContentIdentifierActionEntity;
import com.picpay.banking.reconciliation.entity.ContentIdentifierFileEntity;
import com.picpay.banking.reconciliation.repository.ContentIdentifierActionRepository;
import com.picpay.banking.reconciliation.repository.ContentIdentifierFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Luis Silva
 * @version 1.0 09/12/2020
 */
@ExtendWith(MockitoExtension.class)
class DataBaseContentIdentifierPortTest {

    @Mock
    ContentIdentifierFileRepository contentIdentifierFileRepository;
    @Mock
    ContentIdentifierActionRepository contentIdentifierActionRepository;

    @InjectMocks
    DatabaseContentIdentifierPortImpl databaseContentIdentifierPort;

    ContentIdentifierFile cidFile;

    @BeforeEach
    void init(){

        this.cidFile = ContentIdentifierFile.builder()
            .id(1)
            .status(ContentIdentifierFile.StatusContentIdentifierFile.PROCESSING)
            .keyType(KeyType.CPF)
            .sha256("1232313213")
            .length(1L)
            .requestTime(LocalDateTime.now())
        .build();
    }

    @Test
    void shouldSaveCidFile(){
        when(this.contentIdentifierFileRepository.save(any()))
            .thenReturn(ContentIdentifierFileEntity.from(cidFile));

        this.databaseContentIdentifierPort.saveFile(cidFile);

        verify(this.contentIdentifierFileRepository).save(any());
    }

    @Test
    void shouldFindLastFileRequestedToProcess(){
       when(this.contentIdentifierFileRepository.findFirstByKeyTypeOrderByRequestTimeDesc(any()))
           .thenReturn(Optional.of(ContentIdentifierFileEntity.from(cidFile)));

        final var lastFileRequested = this.databaseContentIdentifierPort.findLastFileRequested(KeyType.CPF);
        assertThat(lastFileRequested).isPresent();

        verify(this.contentIdentifierFileRepository).findFirstByKeyTypeOrderByRequestTimeDesc(any());
    }

    @Test
    void notShouldFindLastFileRequestedToProcess(){
       when(this.contentIdentifierFileRepository.findFirstByKeyTypeOrderByRequestTimeDesc(any()))
           .thenReturn(Optional.empty());

        final var lastFileRequested = this.databaseContentIdentifierPort.findLastFileRequested(KeyType.CPF);
        assertThat(lastFileRequested).isEmpty();

        verify(this.contentIdentifierFileRepository).findFirstByKeyTypeOrderByRequestTimeDesc(any());
    }


    @Test
    void shouldSaveAction(){
        when(this.contentIdentifierActionRepository.save(any())).thenReturn(ContentIdentifierActionEntity.builder()
                                                                           .build());

        final var pixKey = PixKey.builder()
            .key("teste@gmail.com")
            .accountNumber("132")
            .accountOpeningDate(LocalDateTime.now())
            .branchNumber("1")
            .situation(PixKeySituation.ACTIVE)
            .requestId(UUID.randomUUID())
        .build();

        this.databaseContentIdentifierPort.saveAction(1, pixKey,
            "ae843d282551398d7d201be38cb2f6472cfed56aa8a1234612780f9618ec017a", ContentIdentifierFileAction.ADDED);

        verify(this.contentIdentifierActionRepository).save(any());
    }

}
