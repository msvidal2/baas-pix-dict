package com.picpay.banking.claim.ports;

import com.picpay.banking.config.entity.ConfigEntity;
import com.picpay.banking.config.repository.ConfigRepository;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimLastPollingDatePort;
import com.picpay.banking.pix.core.ports.claim.picpay.UpdateClaimLastPollingDatePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateClaimLastPollingDatePortImpl implements UpdateClaimLastPollingDatePort {

    private final ConfigRepository configRepository;

    @Override
    public void update(LocalDateTime endDate) {
        var datetimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        var config = ConfigEntity.builder()
                .key(FindClaimLastPollingDatePort.LAST_POLLING_DATE_KEY)
                .value(datetimeFormatter.format(endDate))
                .build();

        configRepository.save(config);
    }

}
