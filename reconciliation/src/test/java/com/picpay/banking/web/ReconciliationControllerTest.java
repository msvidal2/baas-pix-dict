/*
 *  baas-pix-dict 1.0 23/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.web;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReconciliationControllerTest {

    @Mock
    private FailureReconciliationSyncByFileUseCase syncByCidsFileUseCase;
    @Mock
    private ReconciliationUseCase reconciliationUseCase;
    @InjectMocks
    private ReconciliationController reconciliationController;

    private MockMvc mockMvc;

    public static final String BASE_URL = "/v1/sync";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.
            standaloneSetup(reconciliationController)
            .build();
    }

    @Test
    void when_sync_by_file_requested_then_start_sync() throws Exception {
        mockMvc.perform(post(BASE_URL.concat("/file/CPF")))
            .andDo(print())
            .andExpect(status().isAccepted());

        verify(syncByCidsFileUseCase).execute(KeyType.CPF);
    }

    @Test
    void when_full_sync_requested_then_start_sync() throws Exception {
        mockMvc.perform(post(BASE_URL.concat("/full/CPF")))
            .andDo(print())
            .andExpect(status().isAccepted());

        verify(reconciliationUseCase).execute(KeyType.CPF);

    }

    @Test
    void when_full_sync_requested_with_invalid_type_then_should_fail_with_bad_request() throws Exception {
        mockMvc.perform(post(BASE_URL.concat("/full/XPTO")))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

}