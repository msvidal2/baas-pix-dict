package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoveDatabasePixKeyUseCaseTest {

    @Mock RemovePixKeyPort removePixKeyPort;
    @Mock ContentIdentifierEventPort contentIdentifierEventPort;

    @InjectMocks
    RemoveDatabasePixKeyUseCase removeDatabasePixKeyUseCase;

    @Test
    @DisplayName("Lança exception se o pixKeyEventData não esta preenchido corretamente")
    void exception_when_pixKeyEventData_not_correctly_informed() {
        final var data = PixKeyEventData.builder().build();
        when(removePixKeyPort.remove(any(), any())).thenReturn(Optional.of(PixKey.builder().build()));
        assertThatThrownBy(() -> removeDatabasePixKeyUseCase.execute(data));
    }

    @Test
    @DisplayName("Executa corretamente quando o evento esta completo")
    void should_perform_correctly() {
        var data = PixKeyEventData.builder()
            .ispb(22896431)
            .key("35618656825")
            .type(KeyType.CPF)
            .branchNumber("1")
            .accountNumber("3945812")
            .accountType(AccountType.CHECKING)
            .taxId("35618656825")
            .name("Fulano de Tal da Silva")
            .personType(PersonType.INDIVIDUAL_PERSON)
            .requestId(UUID.fromString("728b45cc-7f8f-43a6-b921-b2f06c85dcfc"))
            .updatedAt(LocalDateTime.now())
            .correlationId("123456789")
            .createdAt(LocalDateTime.now())
            .accountOpeningDate(LocalDateTime.now())
            .startPossessionAt(LocalDateTime.now())
            .build();

        when(removePixKeyPort.remove(any(), any())).thenReturn(
            Optional.of(data.toPixKey().toBuilder()
                .cid("123456")
                .build()));

        removeDatabasePixKeyUseCase.execute(data);

        verify(removePixKeyPort).remove(any(), any());
        verify(contentIdentifierEventPort).save(any());
    }

}
