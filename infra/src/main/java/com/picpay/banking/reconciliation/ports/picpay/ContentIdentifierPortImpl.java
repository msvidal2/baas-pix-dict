package com.picpay.banking.reconciliation.ports.picpay;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierPort;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ContentIdentifierPortImpl implements ContentIdentifierPort {

    private final PixKeyRepository pixKeyRepository;

    @Override
    public Set<String> findAllCidsAfterLastSuccessfulVsync(final KeyType keyType, final LocalDateTime synchronizedAt) {
        return pixKeyRepository.findAllCidsAfterLastSuccessfulVsync(KeyTypeBacen.resolve(keyType), synchronizedAt);
    }

}
