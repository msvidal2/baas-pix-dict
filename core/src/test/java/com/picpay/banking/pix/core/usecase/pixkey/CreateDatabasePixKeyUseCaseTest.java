package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateDatabasePixKeyUseCaseTest {

    @Mock
    SavePixKeyPort savePixKeyPort;

    @Mock
    ContentIdentifierEventPort contentIdentifierEventPort;

    @InjectMocks
    CreateDatabasePixKeyUseCase createDatabasePixKeyUseCase;

    @Test
    @DisplayName("Lança exception se o pixKeyEventData não esta preenchido corretamente")
    void exception_when_pixKeyEventData_not_correctly_informed() {
        final var data = PixKeyEventData.builder().build();
        assertThatThrownBy(() -> createDatabasePixKeyUseCase.execute(data));
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

        createDatabasePixKeyUseCase.execute(data);

        verify(savePixKeyPort).savePixKey(any(), any());
        verify(contentIdentifierEventPort).save(any());
    }

}
