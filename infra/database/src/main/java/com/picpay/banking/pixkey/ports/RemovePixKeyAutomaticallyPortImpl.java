package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.exception.ResourceNotFoundException;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyAutomaticallyPort;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class RemovePixKeyAutomaticallyPortImpl implements RemovePixKeyAutomaticallyPort {

    private final PixKeyRepository pixKeyRepository;

    @Override
    public void remove(String pixKey, LocalDateTime completionThresholdDate) {
        var pixKeyEntity = pixKeyRepository
                .findByIdKeyAndReasonNot(pixKey, Reason.INACTIVITY)
                .orElseThrow(ResourceNotFoundException::new);

        pixKeyEntity.setCompletionPeriodEnd(completionThresholdDate);
        pixKeyEntity.setDonatedAutomatically(true);
        pixKeyEntity.setReason(Reason.INACTIVITY);
        pixKeyRepository.save(pixKeyEntity);
    }

}
