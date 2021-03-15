/*
 *  baas-pix-dict 1.0 23/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.web;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.usecase.reconciliation.FileReconciliationCheckUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.SincronizeCIDEventsUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.SyncVerifierUseCase;
import com.picpay.banking.reconciliation.clients.BacenReconciliationClient;
import com.picpay.banking.reconciliation.repository.SyncVerifierHistoricRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReconciliationControllerTest {

    public static final String BASE_URL = "/v1/sync";
    @Mock
    private FileReconciliationCheckUseCase syncByCidsFileUseCase;
    @Mock
    private SyncVerifierUseCase syncVerifierUseCase;
    @Mock
    private SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase;
    @Mock
    private ReconciliationUseCase reconciliationUseCase;
    @Mock
    private SyncVerifierHistoricRepository syncVerifierHistoricRepository;
    @Mock
    private BacenReconciliationClient bacenReconciliationClient;

    @InjectMocks
    private ReconciliationController reconciliationController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.
            standaloneSetup(reconciliationController)
            .build();
    }

    @Test
    void when_sync_by_file_requested_then_start_sync() throws Exception {
        when(syncVerifierHistoricRepository.save(any())).then(invocation -> invocation.getArgument(0));

        mockMvc.perform(post(BASE_URL.concat("/file/CPF")))
            .andDo(print())
            .andExpect(status().isOk());

        verify(syncByCidsFileUseCase).execute(argThat(syncVerifierHistoric -> syncVerifierHistoric.getKeyType() == KeyType.CPF));
    }

    @Test
    void when_full_sync_requested_then_start_sync() throws Exception {
        mockMvc.perform(post(BASE_URL.concat("/full/CPF")))
            .andDo(print())
            .andExpect(status().isOk());

        verify(reconciliationUseCase).execute(KeyType.CPF);
    }

    @Test
    void when_full_sync_requested_with_invalid_type_then_should_fail_with_bad_request() throws Exception {
        mockMvc.perform(post(BASE_URL.concat("/full/XPTO")))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void when_start_all_key_type_with_full_sync_requested_then_start_all_full_sync() throws Exception {
        mockMvc.perform(post(BASE_URL.concat("/full")))
            .andDo(print())
            .andExpect(status().isOk());

        verify(reconciliationUseCase).execute(KeyType.CPF);
        verify(reconciliationUseCase).execute(KeyType.CNPJ);
        verify(reconciliationUseCase).execute(KeyType.CELLPHONE);
        verify(reconciliationUseCase).execute(KeyType.RANDOM);
        verify(reconciliationUseCase).execute(KeyType.EMAIL);
    }

    @Test
    void when_start_only_sync_verifier_requested_then_start_only_sync_verifier() throws Exception {
        mockMvc.perform(post(BASE_URL.concat("/onlyVerifier/CPF")))
            .andDo(print())
            .andExpect(status().isOk());

        verify(sincronizeCIDEventsUseCase).syncByKeyType(KeyType.CPF);
        verify(syncVerifierUseCase).execute(KeyType.CPF);
    }

}