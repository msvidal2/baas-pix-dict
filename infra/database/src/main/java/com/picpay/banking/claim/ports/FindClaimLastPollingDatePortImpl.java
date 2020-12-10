package com.picpay.banking.claim.ports;

import com.picpay.banking.config.repository.ConfigRepository;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimLastPollingDatePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindClaimLastPollingDatePortImpl implements FindClaimLastPollingDatePort {

    private final ConfigRepository configRepository;

    @Override
    public Optional<LocalDateTime> getDate() {
        var config = configRepository.findById(LAST_POLLING_DATE_KEY);

        if(config.isPresent()) {
            var dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            var date = LocalDateTime.from(dateTimeFormatter.parse(config.get().getValue()));

            return Optional.of(date);
        }

        return Optional.empty();
    }

}
