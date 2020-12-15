package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.CreatePixKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.RemovePixKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.UpdateAccountPixKeyRequestWebDTO;
import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.usecase.pixkey.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.picpay.banking.pix.adapters.incoming.web.helper.ObjectMapperHelper.OBJECT_MAPPER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PixKeyControllerTest {

    public static final String BASE_URL = "/v1/keys";

    private MockMvc mockMvc;

    @InjectMocks
    private PixKeyController controller;

    @Mock
    private CreatePixKeyUseCase createPixKeyUseCase;

    @Mock
    private UpdateAccountPixKeyUseCase updateAccountUseCase;

    @Mock
    private FindPixKeyUseCase findPixKeyUseCase;

    @Mock
    private RemovePixKeyUseCase removePixKeyUseCase;

    @Mock
    private ListPixKeyUseCase listPixKeyUseCase;

    private PixKey pixKey;

    private CreatePixKeyRequestWebDTO createPixKeyDTO;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        pixKey = PixKey.builder()
                .type(KeyType.EMAIL)
                .key("joao@picpay.com")
                .ispb(1)
                .nameIspb("Empresa Picpay")
                .branchNumber("1")
                .accountType(AccountType.SALARY)
                .accountNumber("1")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048")
                .name("Joao da Silva")
                .fantasyName("Nome Fantasia")
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .endToEndId("endToEndId").build();

        createPixKeyDTO = CreatePixKeyRequestWebDTO.builder()
                .type(KeyType.EMAIL)
                .key("teste@teste.com")
                .ispb(12345)
                .branchNumber("0001")
                .accountType(AccountType.SALARY)
                .accountNumber("123456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("31254398713")
                .name("Silva Silva")
                .fantasyName("Fantasy Name")
                .reason(CreateReason.CLIENT_REQUEST)
                .build();
    }

    @Test
    public void when_createPixKeyWithSuccess_expect_statusCreated() throws Exception {
        when(createPixKeyUseCase.execute(anyString(), any(), any())).thenReturn(pixKey);

        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(createPixKeyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.key", equalTo("joao@picpay.com")))
                .andExpect(jsonPath("$.ispb", equalTo(1)))
                .andExpect(jsonPath("$.nameIspb", equalTo("Empresa Picpay")))
                .andExpect(jsonPath("$.accountType", equalTo("SALARY")))
                .andExpect(jsonPath("$.personType", equalTo("INDIVIDUAL_PERSON")));
    }

    @Test
    public void when_updateAccountSuccessfully_expect_statusOk() throws Exception {
        when(updateAccountUseCase.execute(anyString(), any(), any())).thenReturn(pixKey);
        when(findPixKeyUseCase.execute(anyString(), any(), anyString())).thenReturn(pixKey);

        mockMvc.perform(put(BASE_URL +"/joao@picpay")
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(UpdateAccountPixKeyRequestWebDTO.builder()
                        .ispb(1)
                        .accountNumber("15242")
                        .accountOpeningDate(LocalDateTime.now())
                        .accountType(AccountType.SALARY)
                        .branchNumber("4123")
                        .reason(UpdateReason.CLIENT_REQUEST)
                        .type(KeyType.EMAIL)
                        .userId("123")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key", equalTo("joao@picpay.com")))
                .andExpect(jsonPath("$.ispb", equalTo(1)))
                .andExpect(jsonPath("$.nameIspb", equalTo("Empresa Picpay")))
                .andExpect(jsonPath("$.accountType", equalTo("SALARY")))
                .andExpect(jsonPath("$.personType", equalTo("INDIVIDUAL_PERSON")));
    }

    @Test
    public void when_removePixKeySuccessfully_expect_statusNoContent() throws Exception {
        doNothing().when(removePixKeyUseCase).execute(anyString(), any(), any());

        mockMvc.perform(delete(BASE_URL +"/joao@picpay")
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(RemovePixKeyRequestWebDTO.builder()
                        .ispb(1)
                        .reason(RemoveReason.CLIENT_REQUEST)
                        .type(KeyType.EMAIL)
                        .build())))
                .andExpect(status().isNoContent());
    }

    @Test
    public void when_listWithSuccess_expect_statusOk() throws Exception {
        var pixKey1 = PixKey.builder()
                .type(KeyType.EMAIL)
                .key("joao@ppicpay.com")
                .ispb(1)
                .nameIspb("Empresa Picpay")
                .branchNumber("1")
                .accountType(AccountType.SALARY)
                .accountNumber("1")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048L")
                .name("Joao da Silva")
                .fantasyName("Nome Fantasia")
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .endToEndId("endToEndId").build();

        var pixKey2 = PixKey.builder()
                .type(KeyType.CPF)
                .key("57950197048")
                .ispb(1)
                .nameIspb("Empresa Picpay")
                .branchNumber("1")
                .accountType(AccountType.SALARY)
                .accountNumber("1")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048")
                .name("Joao da Silva")
                .fantasyName("Nome Fantasia")
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .endToEndId("endToEndId").build();

        when(listPixKeyUseCase.execute(anyString(), any())).thenReturn(List.of(pixKey1, pixKey2));

        mockMvc.perform(get(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .param("key", "joao@ppicpay.com")
                .param("cpfCnpj", "57950197048")
                .param("personType", "INDIVIDUAL_PERSON")
                .param("accountNumber", "123456")
                .param("accountType", "CHECKING")
                .param("ispb", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].key", equalTo("joao@ppicpay.com")))
                .andExpect(jsonPath("$[1].key", equalTo("57950197048")));
    }

    @Test
    void when_findPixKeyWithSuccess_expect_statusOk() throws Exception {
        when(findPixKeyUseCase.execute(anyString(), any(), anyString()))
                .thenReturn(pixKey);

        mockMvc.perform(get("/v1/keys/joao@picpay")
                .header("requestIdentifier", UUID.randomUUID().toString())
                .header("userId", "59375566072"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key", equalTo("joao@picpay.com")))
                .andExpect(jsonPath("$.type", equalTo("EMAIL")))
                .andExpect(jsonPath("$.taxId", equalTo("57950197048")))
                .andExpect(jsonPath("$.personType", equalTo("INDIVIDUAL_PERSON")))
                .andExpect(jsonPath("$.accountType", equalTo("SALARY")));
    }

    @Test
    public void when_createPixKeyWithNullType_expect_statusBadRequest() throws Exception {
        createPixKeyDTO.setType(null);

        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(createPixKeyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid Arguments")))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("type")))
                .andExpect(jsonPath("$.fieldErrors[0].message", equalTo("must not be null")));
    }

    @Test
    public void when_createPixKeyWithNullIspb_expect_statusBadRequest() throws Exception {
        createPixKeyDTO.setIspb(null);

        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(createPixKeyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid Arguments")))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("ispb")))
                .andExpect(jsonPath("$.fieldErrors[0].message", equalTo("must not be null")));
    }

    @Test
    public void when_createPixKeyWithNullAccountType_expect_statusBadRequest() throws Exception {
        createPixKeyDTO.setAccountType(null);

        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(createPixKeyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid Arguments")))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("accountType")))
                .andExpect(jsonPath("$.fieldErrors[0].message", equalTo("must not be null")));
    }

    @Test
    public void when_createPixKeyWithNullAccountNumber_expect_statusBadRequest() throws Exception {
        createPixKeyDTO.setAccountNumber(null);

        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(createPixKeyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid Arguments")))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("accountNumber")))
                .andExpect(jsonPath("$.fieldErrors[0].message", equalTo("must not be null")));
    }

    @Test
    public void when_createPixKeyWithNullAccountOpeningDate_expect_statusBadRequest() throws Exception {
        createPixKeyDTO.setAccountOpeningDate(null);

        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(createPixKeyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid Arguments")))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("accountOpeningDate")))
                .andExpect(jsonPath("$.fieldErrors[0].message", equalTo("must not be null")));
    }

    @Test
    public void when_createPixKeyWithNullPersonType_expect_statusBadRequest() throws Exception {
        createPixKeyDTO.setPersonType(null);

        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(createPixKeyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid Arguments")))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("personType")))
                .andExpect(jsonPath("$.fieldErrors[0].message", equalTo("must not be null")));
    }

    @Test
    public void when_createPixKeyWithNullCpfCnpj_expect_statusBadRequest() throws Exception {
        createPixKeyDTO.setCpfCnpj(null);

        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(createPixKeyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid Arguments")))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("cpfCnpj")))
                .andExpect(jsonPath("$.fieldErrors[0].message", equalTo("must not be null")));
    }

    @Test
    public void when_createPixKeyWithNullName_expect_statusBadRequest() throws Exception {
        createPixKeyDTO.setName(null);

        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(createPixKeyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid Arguments")))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", equalTo("must not be null")));
    }

    @Test
    public void when_createPixKeyWithNullReason_expect_statusBadRequest() throws Exception {
        createPixKeyDTO.setReason(null);

        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(createPixKeyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid Arguments")))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("reason")))
                .andExpect(jsonPath("$.fieldErrors[0].message", equalTo("must not be null")));
    }

    @Test
    public void when_createPixKeyWithNullRequestIdentifier_expect_statusBadRequest() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(createPixKeyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message",
                        equalTo("Missing request header 'requestIdentifier' for method parameter of type String")));
    }

}