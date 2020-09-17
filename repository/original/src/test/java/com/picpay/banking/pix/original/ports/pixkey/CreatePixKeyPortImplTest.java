package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.original.clients.AccessKeyClient;
import com.picpay.banking.pix.original.dto.response.AccessKeyCreateDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.core.domain.CreateReason.CUSTOMER_REQUEST;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePixKeyPortImplTest {

    @InjectMocks
    private CreatePixKeyPortImpl port;

    @Mock
    private AccessKeyClient maintenancePixKeyClient;

    private ResponseWrapperDTO<AccessKeyCreateDTO> responseWrapper;

    @BeforeEach
    void setup() {
        var accessKey = AccessKeyCreateDTO.builder()
                .creationDate(LocalDateTime.now())
                .key("26581707007")
                .keyOwnershipDate(LocalDateTime.now())
                .returnCode(200)
                .returnMessage("OK")
                .build();

        responseWrapper = ResponseWrapperDTO.<AccessKeyCreateDTO>builder()
                .data(accessKey)
                .build();
    }

    @Test
    void when_createNonRandomKeyWithSuccess_expect_pixKeyWithCreatedAtAndKey() {
        when(maintenancePixKeyClient.createPixKey(anyString(), any()))
                .thenReturn(responseWrapper);

        var pixKey = PixKey.builder()
                .key("26581707007")
                .type(KeyType.CPF)
                .ispb(34534543)
                .nameIspb("PicPay")
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber("123456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("26581707007")
                .name("Maria Aparecida")
                .fantasyName("Maria Aparecida")
                .endToEndId("kiuhnfkv8743yt8347ynd738f3885yi34")
                .build();

        assertDoesNotThrow(() -> {
            var response = port.createPixKey(randomUUID().toString(), pixKey, CUSTOMER_REQUEST);

            assertNotNull(response);
            assertEquals(responseWrapper.getData().getKey(), response.getKey());
            assertEquals(responseWrapper.getData().getCreationDate(), response.getCreatedAt());
            assertEquals(responseWrapper.getData().getKeyOwnershipDate(), response.getStartPossessionAt());
        });
    }

    @Test
    void when_createRandomKeyWithSuccess_expect_pixKeyWithCreatedAtAndKey() {
        var accessKey = AccessKeyCreateDTO.builder()
                .creationDate(LocalDateTime.now())
                .key(randomUUID().toString())
                .keyOwnershipDate(LocalDateTime.now())
                .returnCode(200)
                .returnMessage("OK")
                .build();

        var localResponseWrapper = ResponseWrapperDTO.<AccessKeyCreateDTO>builder()
                .data(accessKey)
                .build();

        when(maintenancePixKeyClient.createEvpPixKey(anyString(), any()))
                .thenReturn(localResponseWrapper);

        var pixKey = PixKey.builder()
                .type(KeyType.RANDOM)
                .ispb(34534543)
                .nameIspb("PicPay")
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber("123456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("26581707007")
                .name("Maria Aparecida")
                .fantasyName("Maria Aparecida")
                .endToEndId("kiuhnfkv8743yt8347ynd738f3885yi34")
                .build();

        assertDoesNotThrow(() -> {
            var response = port.createPixKey(randomUUID().toString(), pixKey, CUSTOMER_REQUEST);

            assertNotNull(response);
            assertEquals(localResponseWrapper.getData().getKey(), response.getKey());
            assertEquals(localResponseWrapper.getData().getCreationDate(), response.getCreatedAt());
            assertEquals(localResponseWrapper.getData().getKeyOwnershipDate(), response.getStartPossessionAt());
        });
    }

}