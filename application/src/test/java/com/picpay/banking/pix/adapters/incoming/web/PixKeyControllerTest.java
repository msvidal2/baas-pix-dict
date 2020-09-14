package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.CreatePixKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.UpdateAccountPixKeyDTO;
import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.usecase.FindPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.UpdateAccountPixKeyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.adapters.incoming.web.helper.ObjectMapperHelper.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PixKeyControllerTest {

    public static final String BASE_URL = "/v1/keys";

    private MockMvc mockMvc;

    @InjectMocks
    private PixKeyController controller;

    @Mock
    private UpdateAccountPixKeyUseCase updateAccountUseCase;

    @Mock
    private FindPixKeyUseCase findPixKeyUseCase;

    private PixKey pixKey;

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
                .taxId("57950197048L")
                .name("Joao da Silva")
                .fantasyName("Nome Fantasia")
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .endToEndId("endToEndId").build();
    }

    @Test
    public void when_updateAccountSuccessfully_expect_statusOk() throws Exception {
        when(updateAccountUseCase.update(any(), any(), anyString())).thenReturn(pixKey);
        when(findPixKeyUseCase.findPixKeyUseCase(any(), anyString())).thenReturn(pixKey);

        mockMvc.perform(put(BASE_URL +"/joao@picpay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(UpdateAccountPixKeyDTO.builder()
                        .ispb(1)
                        .accountNumber("15242")
                        .accountOpeningDate(LocalDateTime.now())
                        .accountType(AccountType.SALARY)
                        .branchNumber("4123")
                        .reason(UpdateReason.CLIENT_REQUEST)
                        .type(KeyType.EMAIL)
                        .requestIdentifier("abc")
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
    public void when_createPixKeyWithNullType_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setKey("teste@teste.com");
        dto.setIspb(12345);
        dto.setBranchNumber("0001");
        dto.setAccountType(AccountType.SALARY);
        dto.setAccountNumber("123456");
        dto.setAccountOpeningDate(LocalDateTime.now());
        dto.setPersonType(PersonType.INDIVIDUAL_PERSON);
        dto.setCpfCnpj("31254398713");
        dto.setName("Silva Silva");
        dto.setFantasyName("Fantasy Name");
        dto.setReason(CreateReason.CUSTOMER_REQUEST);
        dto.setRequestIdentifier("1234");

        assertController(dto, status().is4xxClientError());
    }

    @Test
    public void when_createPixKeyWithNullKey_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setType(KeyType.EMAIL);
        dto.setIspb(12345);
        dto.setBranchNumber("0001");
        dto.setAccountType(AccountType.SALARY);
        dto.setAccountNumber("123456");
        dto.setAccountOpeningDate(LocalDateTime.now());
        dto.setPersonType(PersonType.INDIVIDUAL_PERSON);
        dto.setCpfCnpj("31254398713");
        dto.setName("Silva Silva");
        dto.setFantasyName("Fantasy Name");
        dto.setReason(CreateReason.CUSTOMER_REQUEST);
        dto.setRequestIdentifier("1234");

        assertController(dto, status().is4xxClientError());
    }

    @Test
    public void when_createPixKeyWithNullIspb_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setType(KeyType.EMAIL);
        dto.setKey("teste@teste.com");
        dto.setBranchNumber("0001");
        dto.setAccountType(AccountType.SALARY);
        dto.setAccountNumber("123456");
        dto.setAccountOpeningDate(LocalDateTime.now());
        dto.setPersonType(PersonType.INDIVIDUAL_PERSON);
        dto.setCpfCnpj("31254398713");
        dto.setName("Silva Silva");
        dto.setFantasyName("Fantasy Name");
        dto.setReason(CreateReason.CUSTOMER_REQUEST);
        dto.setRequestIdentifier("1234");

        assertController(dto, status().is4xxClientError());
    }

    @Test
    public void when_createPixKeyWithNullAccountType_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setType(KeyType.EMAIL);
        dto.setKey("teste@teste.com");
        dto.setIspb(12345);
        dto.setBranchNumber("0001");
        dto.setAccountNumber("123456");
        dto.setAccountOpeningDate(LocalDateTime.now());
        dto.setPersonType(PersonType.INDIVIDUAL_PERSON);
        dto.setCpfCnpj("31254398713");
        dto.setName("Silva Silva");
        dto.setFantasyName("Fantasy Name");
        dto.setReason(CreateReason.CUSTOMER_REQUEST);
        dto.setRequestIdentifier("1234");

        assertController(dto, status().is4xxClientError());
    }

    @Test
    public void when_createPixKeyWithNullAccountNumber_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setType(KeyType.EMAIL);
        dto.setKey("teste@teste.com");
        dto.setIspb(12345);
        dto.setBranchNumber("0001");
        dto.setAccountType(AccountType.SALARY);
        dto.setAccountOpeningDate(LocalDateTime.now());
        dto.setPersonType(PersonType.INDIVIDUAL_PERSON);
        dto.setCpfCnpj("31254398713");
        dto.setName("Silva Silva");
        dto.setFantasyName("Fantasy Name");
        dto.setReason(CreateReason.CUSTOMER_REQUEST);
        dto.setRequestIdentifier("1234");

        assertController(dto, status().is4xxClientError());
    }

    @Test
    public void when_createPixKeyWithNullAccountOpeningDate_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setType(KeyType.EMAIL);
        dto.setKey("teste@teste.com");
        dto.setIspb(12345);
        dto.setBranchNumber("0001");
        dto.setAccountType(AccountType.SALARY);
        dto.setAccountNumber("123456");
        dto.setPersonType(PersonType.INDIVIDUAL_PERSON);
        dto.setCpfCnpj("31254398713");
        dto.setName("Silva Silva");
        dto.setFantasyName("Fantasy Name");
        dto.setReason(CreateReason.CUSTOMER_REQUEST);
        dto.setRequestIdentifier("1234");

        assertController(dto, status().is4xxClientError());
    }

    @Test
    public void when_createPixKeyWithNullPersonType_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setType(KeyType.EMAIL);
        dto.setKey("teste@teste.com");
        dto.setIspb(12345);
        dto.setBranchNumber("0001");
        dto.setAccountType(AccountType.SALARY);
        dto.setAccountNumber("123456");
        dto.setAccountOpeningDate(LocalDateTime.now());
        dto.setCpfCnpj("31254398713");
        dto.setName("Silva Silva");
        dto.setFantasyName("Fantasy Name");
        dto.setReason(CreateReason.CUSTOMER_REQUEST);
        dto.setRequestIdentifier("1234");

        assertController(dto, status().is4xxClientError());
    }

    @Test
    public void when_createPixKeyWithNullCpfCnpj_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setType(KeyType.EMAIL);
        dto.setKey("teste@teste.com");
        dto.setIspb(12345);
        dto.setBranchNumber("0001");
        dto.setAccountType(AccountType.SALARY);
        dto.setAccountNumber("123456");
        dto.setAccountOpeningDate(LocalDateTime.now());
        dto.setPersonType(PersonType.INDIVIDUAL_PERSON);
        dto.setName("Silva Silva");
        dto.setFantasyName("Fantasy Name");
        dto.setReason(CreateReason.CUSTOMER_REQUEST);
        dto.setRequestIdentifier("1234");

        assertController(dto, status().is4xxClientError());
    }

    @Test
    public void when_createPixKeyWithNullName_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setType(KeyType.EMAIL);
        dto.setKey("teste@teste.com");
        dto.setIspb(12345);
        dto.setBranchNumber("0001");
        dto.setAccountType(AccountType.SALARY);
        dto.setAccountNumber("123456");
        dto.setAccountOpeningDate(LocalDateTime.now());
        dto.setPersonType(PersonType.INDIVIDUAL_PERSON);
        dto.setCpfCnpj("31254398713");
        dto.setFantasyName("Fantasy Name");
        dto.setReason(CreateReason.CUSTOMER_REQUEST);
        dto.setRequestIdentifier("1234");

        assertController(dto, status().is4xxClientError());
    }

    @Test
    public void when_createPixKeyWithNullReason_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setType(KeyType.EMAIL);
        dto.setKey("teste@teste.com");
        dto.setIspb(12345);
        dto.setBranchNumber("0001");
        dto.setAccountType(AccountType.SALARY);
        dto.setAccountNumber("123456");
        dto.setAccountOpeningDate(LocalDateTime.now());
        dto.setPersonType(PersonType.INDIVIDUAL_PERSON);
        dto.setCpfCnpj("31254398713");
        dto.setName("Silva Silva");
        dto.setFantasyName("Fantasy Name");
        dto.setRequestIdentifier("1234");

        assertController(dto, status().is4xxClientError());
    }

    @Test
    public void when_createPixKeyWithNullRequestIdentifier_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setType(KeyType.EMAIL);
        dto.setKey("teste@teste.com");
        dto.setIspb(12345);
        dto.setBranchNumber("0001");
        dto.setAccountType(AccountType.SALARY);
        dto.setAccountNumber("123456");
        dto.setAccountOpeningDate(LocalDateTime.now());
        dto.setPersonType(PersonType.INDIVIDUAL_PERSON);
        dto.setCpfCnpj("31254398713");
        dto.setName("Silva Silva");
        dto.setFantasyName("Fantasy Name");
        dto.setReason(CreateReason.CUSTOMER_REQUEST);

        assertController(dto, status().is4xxClientError());
    }

    @Test
    public void when_createPixKeyWithSuccess_expect_NullPointer() throws Exception {
        CreatePixKeyRequestWebDTO dto = new CreatePixKeyRequestWebDTO();
        dto.setType(KeyType.EMAIL);
        dto.setKey("teste@teste.com");
        dto.setIspb(12345);
        dto.setBranchNumber("0001");
        dto.setAccountType(AccountType.SALARY);
        dto.setAccountNumber("123456");
        dto.setAccountOpeningDate(LocalDateTime.now());
        dto.setPersonType(PersonType.INDIVIDUAL_PERSON);
        dto.setCpfCnpj("31254398713");
        dto.setName("Silva Silva");
        dto.setFantasyName("Fantasy Name");
        dto.setReason(CreateReason.CUSTOMER_REQUEST);
        dto.setRequestIdentifier("1234");

        assertController(dto, status().isCreated());
    }

    private void assertController(CreatePixKeyRequestWebDTO dto, ResultMatcher match) throws Exception {
        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(dto)))
                .andDo(print())
                .andExpect(match);

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
    }

}