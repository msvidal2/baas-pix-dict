package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.ContentIdentifierAction;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.reconciliation.entity.ContentIdentifierActionEntity;
import com.picpay.banking.reconciliation.entity.ContentIdentifierEntity;
import com.picpay.banking.reconciliation.entity.ContentIdentifierFileEntity;
import com.picpay.banking.reconciliation.repository.ContentIdentifierActionRepository;
import com.picpay.banking.reconciliation.repository.ContentIdentifierFileRepository;
import com.picpay.banking.reconciliation.repository.ContentIdentifierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Luis Silva
 * @version 1.0 09/12/2020
 */
@ExtendWith(MockitoExtension.class)
public class DataBaseContentIdentifierPortTest {

    @Mock
    private ContentIdentifierFileRepository contentIdentifierFileRepository;
    @Mock
    private ContentIdentifierRepository contentIdentifierRepository;
    @Mock
    private ContentIdentifierActionRepository contentIdentifierActionRepository;

    @InjectMocks
    private DatabaseContentIdentifierPortImpl databaseContentIdentifierPort;

    private ContentIdentifier cid;
    private ContentIdentifierFile cidFile;

    @BeforeEach
    public void init(){
        this.cid = ContentIdentifier.builder()
            .cid("ae843d282551398d7d201be38cb2f6472cfed56aa8a1234612780f9618ec017a")
            .key("teste@gmail.com")
            .build();

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
    public void shouldSaveCid(){
        when(this.contentIdentifierRepository.save(any()))
            .thenReturn(ContentIdentifierEntity.from(cid));

        this.databaseContentIdentifierPort.save(cid);

        verify(this.contentIdentifierRepository).save(any());
    }

    @Test
    public void shouldSaveCidFile(){
        when(this.contentIdentifierFileRepository.save(any()))
            .thenReturn(ContentIdentifierFileEntity.from(cidFile));

        this.databaseContentIdentifierPort.saveFile(cidFile);

        verify(this.contentIdentifierFileRepository).save(any());
    }

    @Test
    public void shouldFindCid(){
        when(this.contentIdentifierRepository.findById(any()))
            .thenReturn(Optional.of(ContentIdentifierEntity.from(cid)));

        final var cidDomain = this.databaseContentIdentifierPort.findByCid(
            "ae843d282551398d7d201be38cb2f6472cfed56aa8a1234612780f9618ec017a");
        assertThat(cidDomain).isPresent();


        verify(this.contentIdentifierRepository).findById(any());
    }

     @Test
    public void notShouldFindCid(){
        when(this.contentIdentifierRepository.findById(any()))
            .thenReturn(Optional.empty());

        final var cidDomain = this.databaseContentIdentifierPort.findByCid(
            "ae843d282551398d7d201be38cb2f6472cfed56aa8a1234612780f9618ec017a");
        assertThat(cidDomain).isEmpty();

        verify(this.contentIdentifierRepository).findById(any());
    }

    @Test
    public void shouldDeleteCid(){
        doNothing().when(this.contentIdentifierRepository).deleteById(any());

        this.databaseContentIdentifierPort.delete(
            "ae843d282551398d7d201be38cb2f6472cfed56aa8a1234612780f9618ec017a");

        verify(this.contentIdentifierRepository).deleteById(any());
    }

    @Test
    public void shouldFindLastFileRequestedToProcess(){
       when(this.contentIdentifierFileRepository.findFirstByKeyTypeAndStatusInOrderByRequestTimeDesc(any(),anyList()))
           .thenReturn(Optional.of(ContentIdentifierFileEntity.from(cidFile)));

        final var lastFileRequested = this.databaseContentIdentifierPort.findLastFileRequested(KeyType.CPF);
        assertThat(lastFileRequested).isPresent();

        verify(this.contentIdentifierFileRepository).findFirstByKeyTypeAndStatusInOrderByRequestTimeDesc(any(),anyList());
    }

    @Test
    public void notShouldFindLastFileRequestedToProcess(){
       when(this.contentIdentifierFileRepository.findFirstByKeyTypeAndStatusInOrderByRequestTimeDesc(any(),anyList()))
           .thenReturn(Optional.empty());

        final var lastFileRequested = this.databaseContentIdentifierPort.findLastFileRequested(KeyType.CPF);
        assertThat(lastFileRequested).isEmpty();

        verify(this.contentIdentifierFileRepository).findFirstByKeyTypeAndStatusInOrderByRequestTimeDesc(any(),anyList());
    }

    @Test
    public void shouldListAllCids(){
       when(this.contentIdentifierRepository.findAllByKeyType(any()))
            .thenReturn(List.of(ContentIdentifierEntity.from(cid)));

        final var cids = this.databaseContentIdentifierPort.listAll(KeyType.CPF);
        assertThat(cids).isNotNull();
        assertThat(cids).isNotEmpty();

        verify(this.contentIdentifierRepository).findAllByKeyType(any());
    }

    @Test
    public void shouldSaveAction(){
        when(this.contentIdentifierActionRepository.save(any())).thenReturn(ContentIdentifierActionEntity.builder()
                                                                           .build());

        final var pixKey = PixKey.builder()
            .key("teste@gmail.com")
            .accountNumber("132")
            .accountOpeningDate(LocalDateTime.now())
            .branchNumber("1")
            .requestId(UUID.randomUUID())
        .build();

        this.databaseContentIdentifierPort.saveAction(1, pixKey,
            "ae843d282551398d7d201be38cb2f6472cfed56aa8a1234612780f9618ec017a", ContentIdentifierAction.ADDED);

        verify(this.contentIdentifierActionRepository).save(any());
    }

    @Test
    public void shouldFindByKey(){
        when(this.contentIdentifierRepository.findByKey(any()))
            .thenReturn(Optional.of(ContentIdentifierEntity.from(cid)));

        final var cid = this.databaseContentIdentifierPort.findByKey("teste@gmail.com");
        assertThat(cid).isPresent();

        verify(this.contentIdentifierRepository).findByKey(any());
    }

    @Test
    public void notShouldFindByKey(){
        when(this.contentIdentifierRepository.findByKey(any()))
            .thenReturn(Optional.empty());

        final var cid = this.databaseContentIdentifierPort.findByKey("teste@gmail.com");
        assertThat(cid).isEmpty();

        verify(this.contentIdentifierRepository).findByKey(any());
    }

}
