package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.exception.PixKeyError;
import com.picpay.banking.pix.core.exception.PixKeyException;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.validators.pixkey.CreatePixKeyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class CreatePixKeyBacenUseCase {

    private final CreatePixKeyBacenPort createPixKeyBacenPortBacen;
    private final FindPixKeyPort findPixKeyPort;
    private final FindOpenClaimByKeyPort findOpenClaimByKeyPort;

    public PixKey execute(final String requestIdentifier, final PixKey pixKey, final Reason reason) {
        CreatePixKeyValidator.validate(requestIdentifier, pixKey, reason);

        var pixKeysExisting = validateNumberKeys(pixKey);
        validateRegisteredAccountForDifferentPerson(pixKey, pixKeysExisting);
        validateKeyExists(pixKey);
        validateClaimExists(pixKey);

        var createdPixKey = createPixKeyBacenPortBacen.create(requestIdentifier, pixKey, reason);
        createdPixKey.calculateCid();

        log.info("PixKey_created"
            , kv("requestIdentifier", requestIdentifier)
            , kv("key", createdPixKey.getKey()));

        return createdPixKey;
    }

    private List<PixKey> validateNumberKeys(final PixKey pixKey) {
        int maxKeysPerAccount = 5;

        if (PersonType.LEGAL_ENTITY.equals(pixKey.getPersonType())) {
            maxKeysPerAccount = 20;
        }

        var pixKeysExisting = findPixKeyPort.findByAccount(
            pixKey.getIspb(),
            pixKey.getBranchNumber(),
            pixKey.getAccountNumber(),
            pixKey.getAccountType());

        if (pixKeysExisting.size() >= maxKeysPerAccount) {
            throw new PixKeyException("The maximum number of keys cannot be greater than " + maxKeysPerAccount);
        }

        return pixKeysExisting;
    }

    private void validateRegisteredAccountForDifferentPerson(PixKey pixKey, List<PixKey> pixKeysExisting) {
        pixKeysExisting.stream().findAny()
            .ifPresent(pk -> {
                if (!pixKey.getTaxIdWithLeftZeros().equals(pk.getTaxIdWithLeftZeros())) {
                    throw new PixKeyException(PixKeyError.EXISTING_ACCOUNT_REGISTRATION_FOR_ANOTHER_PERSON);
                }
            });
    }

    private void validateKeyExists(final PixKey pixKey) {
        var optionalPixKey = findPixKeyPort.findPixKey(pixKey.getKey());
        optionalPixKey.ifPresent(pixKeyExisting -> {

            if (pixKey.getTaxIdWithLeftZeros().equals(pixKeyExisting.getTaxIdWithLeftZeros())) {
                if (pixKey.equals(pixKeyExisting)) {
                    throw new PixKeyException(PixKeyError.KEY_EXISTS);
                }

                throw new PixKeyException(PixKeyError.KEY_EXISTS_INTO_PSP_TO_SAME_PERSON);
            }

            throw new PixKeyException(PixKeyError.KEY_EXISTS_INTO_PSP_TO_ANOTHER_PERSON);
        });
    }

    private void validateClaimExists(final PixKey pixKey) {
        var claim = findOpenClaimByKeyPort.find(pixKey.getKey());

        if (claim.isPresent()) {
            throw new PixKeyException(PixKeyError.CLAIM_PROCESS_EXISTING);
        }
    }

}
