package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.exception.PixKeyError;
import com.picpay.banking.pix.core.exception.PixKeyException;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.validators.pixkey.CreatePixKeyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class CreatePixKeyUseCase {

    public static final String REQUEST_IDENTIFIER = "requestIdentifier";
    public static final String KEY = "key";
    public static final String EXCEPTION = "exception";
    private final CreatePixKeyBacenPort createPixKeyBacenPortBacen;
    private final SavePixKeyPort savePixKeyPort;
    private final FindPixKeyPort findPixKeyPort;
    private final FindOpenClaimByKeyPort findOpenClaimByKeyPort;
    private final PixKeyEventPort pixKeyEventPort;

    public PixKey execute(final String requestIdentifier,
        final PixKey pixKey,
        final CreateReason reason) {

        CreatePixKeyValidator.validate(requestIdentifier, pixKey, reason);

        var pixKeysExisting = validateNumberKeys(pixKey);
        validateRegisteredAccountForDifferentPerson(pixKey, pixKeysExisting);
        validateKeyExists(pixKey);
        validateClaimExists(pixKey);

        var createdPixKey = createPixKeyBacenPortBacen.create(requestIdentifier, pixKey, reason);
        createdPixKey.calculateCid();

        save(reason, createdPixKey, requestIdentifier);
        sendEvent(createdPixKey, requestIdentifier);

        log.info("PixKey_created"
            , kv(REQUEST_IDENTIFIER, requestIdentifier)
            , kv(KEY, createdPixKey.getKey()));

        return createdPixKey;
    }

    private void save(CreateReason reason, PixKey createdPixKey, final String requestIdentifier) {
        try {
            savePixKeyPort.savePixKey(createdPixKey, reason.getValue());
        } catch (Exception e) {
            log.error("PixKey_create_saveError",
                    kv(REQUEST_IDENTIFIER, requestIdentifier),
                    kv(KEY, createdPixKey.getKey()),
                    kv(EXCEPTION, e));
        }
    }

    private void sendEvent(PixKey createdPixKey, final String requestIdentifier) {
        try {
            pixKeyEventPort.pixKeyWasCreated(createdPixKey);
        } catch (Exception e) {
            log.error("PixKey_create_eventError",
                    kv(REQUEST_IDENTIFIER, requestIdentifier),
                    kv(KEY, createdPixKey.getKey()),
                    kv(EXCEPTION, e));
        }
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
