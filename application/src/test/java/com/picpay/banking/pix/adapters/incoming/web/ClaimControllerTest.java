package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.usecase.claim.CreateClaimUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class ClaimControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ClaimController controller;

    @Mock
    private CreateClaimUseCase createAddressKeyUseCase;

    private Claim claim;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.
                standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        claim = Claim.builder()
                .build();
    }

    @Test
    void when_createClaimWithSuccess_expect_statusCreated() {
        fail();
    }

}