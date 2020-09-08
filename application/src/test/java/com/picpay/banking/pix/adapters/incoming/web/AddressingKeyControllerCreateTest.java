package com.picpay.banking.pix.adapters.incoming.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.picpay.banking.pix.adapters.incoming.web.dto.CreateAddressingKeyRequestWebDTO;
import com.picpay.banking.pix.converters.CreateAddressingKeyWebConverter;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.usecase.CreateAddressingKeyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AddressingKeyControllerCreateTest {

    public static final String BASE_URL = "/v1/addressing-keys";

    @InjectMocks
    private AddressingKeyController createController;

    @Mock
    private CreateAddressingKeyUseCase useCase;

    @Mock
    private CreateAddressingKeyWebConverter converter;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(createController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    @Test
    public void when_createAddressingKeyWithNullType_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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
    public void when_createAddressingKeyWithNullKey_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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
    public void when_createAddressingKeyWithNullIspb_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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
    public void when_createAddressingKeyWithNullAccountType_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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
    public void when_createAddressingKeyWithNullAccountNumber_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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
    public void when_createAddressingKeyWithNullAccountOpeningDate_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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
    public void when_createAddressingKeyWithNullPersonType_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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
    public void when_createAddressingKeyWithNullCpfCnpj_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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
    public void when_createAddressingKeyWithNullName_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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
    public void when_createAddressingKeyWithNullReason_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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
    public void when_createAddressingKeyWithNullRequestIdentifier_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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
    public void when_createAddressingKeyWithSuccess_expect_NullPointer() throws Exception {
        CreateAddressingKeyRequestWebDTO dto = new CreateAddressingKeyRequestWebDTO();
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

    private void assertController(CreateAddressingKeyRequestWebDTO dto, ResultMatcher match) throws Exception {
        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andDo(print())
                .andExpect(match);

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
    }

    public static String asJsonString(final Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
