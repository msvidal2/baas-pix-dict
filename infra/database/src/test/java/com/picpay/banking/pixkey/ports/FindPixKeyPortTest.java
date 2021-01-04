package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Luis Silva
 * @version 1.0 18/12/2020
 */
@ExtendWith(MockitoExtension.class)
public class FindPixKeyPortTest {

    @InjectMocks
    private FindPixKeyPortImpl findPixKeyPort;

    @Mock
    private PixKeyRepository pixKeyRepository;

    private PixKey pixKey;

    @BeforeEach
    public void init(){
        this.pixKey =  PixKey.builder()
            .key("teste@gmail.com")
            .type(KeyType.EMAIL)
            .accountNumber("132")
            .accountOpeningDate(LocalDateTime.now())
            .branchNumber("1")
            .requestId(UUID.randomUUID())
            .cid("ae843d282551398d7d201be38cb2f6472cfed56aa8a1234612780f9618ec017a")
        .build();
    }

    @Test
    void shouldFindCid(){
        when(this.pixKeyRepository.findByCidAndDonatedAutomaticallyFalse(any()))
            .thenReturn(Optional.of(PixKeyEntity.from(pixKey, Reason.RECONCILIATION)));

        final var cidDomain = this.findPixKeyPort.findByCid(
            "ae843d282551398d7d201be38cb2f6472cfed56aa8a1234612780f9618ec017a");
        assertThat(cidDomain).isPresent();


        verify(this.pixKeyRepository).findByCidAndDonatedAutomaticallyFalse(any());
    }

    @Test
    void notShouldFindCid(){
        when(this.pixKeyRepository.findByCidAndDonatedAutomaticallyFalse(any()))
            .thenReturn(Optional.empty());

        final var cidDomain = this.findPixKeyPort.findByCid(
            "ae843d282551398d7d201be38cb2f6472cfed56aa8a1234612780f9618ec017a");
        assertThat(cidDomain).isEmpty();

        verify(this.pixKeyRepository).findByCidAndDonatedAutomaticallyFalse(any());
    }


    @Test
    void shouldListAllCids(){
        when(this.pixKeyRepository.findAllByIdTypeAndDonatedAutomaticallyFalse(any()))
            .thenReturn(List.of(PixKeyEntity.from(pixKey, Reason.RECONCILIATION)));

        final var cids = this.findPixKeyPort.findAllByKeyType(KeyType.EMAIL);
        assertThat(cids).isNotNull().isNotEmpty();

        verify(this.pixKeyRepository).findAllByIdTypeAndDonatedAutomaticallyFalse(any());
    }

    @Test
    void shouldFindByKey(){
        when(this.pixKeyRepository.findByIdKeyAndDonatedAutomaticallyFalse(any()))
            .thenReturn(Optional.of(PixKeyEntity.from(pixKey, Reason.RECONCILIATION)));

        final var cid = this.findPixKeyPort.findPixKey("teste@gmail.com");
        assertThat(cid).isPresent();

        verify(this.pixKeyRepository).findByIdKeyAndDonatedAutomaticallyFalse(any());
    }

    @Test
    void notShouldFindByKey(){
        when(this.pixKeyRepository.findByIdKeyAndDonatedAutomaticallyFalse(any()))
            .thenReturn(Optional.empty());

        final var cid = this.findPixKeyPort.findPixKey("teste@gmail.com");
        assertThat(cid).isEmpty();

        verify(this.pixKeyRepository).findByIdKeyAndDonatedAutomaticallyFalse(any());
    }


}
