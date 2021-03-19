package com.picpay.banking.workers.pixkeydatabase.service;

import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.usecase.pixkey.CreateDatabasePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemoveDatabasePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.UpdateDatabasePixKeyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseService {

    private final CreateDatabasePixKeyUseCase createDatabasePixKeyUseCase;
    private final UpdateDatabasePixKeyUseCase updateDatabasePixKeyUseCase;
    private final RemoveDatabasePixKeyUseCase removeDatabasePixKeyUseCase;

    @Transactional
    public void create(PixKeyEventData pixKeyEventData) {
        createDatabasePixKeyUseCase.execute(pixKeyEventData);
    }

    @Transactional
    public void update(PixKeyEventData pixKeyEventData) {
        updateDatabasePixKeyUseCase.execute(pixKeyEventData);
    }

    @Transactional
    public void remove(PixKeyEventData pixKeyEventData) {
        removeDatabasePixKeyUseCase.execute(pixKeyEventData);
    }

}
