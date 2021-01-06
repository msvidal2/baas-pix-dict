package com.picpay.banking.pix.core.domain;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@EqualsAndHashCode(of = {"type", "key", "ispb", "branchNumber", "accountType", "accountNumber", "personType", "taxId"})
public class PixKey {

    private static final String SEPARATOR = "&";

    private KeyType type;
    private String key;
    private Integer ispb;
    private String nameIspb;
    private String branchNumber;
    private AccountType accountType;
    private String accountNumber;
    private LocalDateTime accountOpeningDate;
    private PersonType personType;
    private String taxId;
    private String name;
    private String fantasyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime startPossessionAt;
    private String endToEndId;
    private String correlationId;
    private ClaimType claim;
    private Statistic statistic;
    private String cid;
    private UUID requestId;
    private boolean donatedAutomatically;

    public String getOwnerName() {
        if (PersonType.INDIVIDUAL_PERSON.equals(personType)) {
            return name;
        }

        return ObjectUtils.firstNonNull(fantasyName, name);
    }

    public String getTaxIdWithLeftZeros() {
        int size = 11;

        if (PersonType.LEGAL_ENTITY.equals(personType)) {
            size = 14;
        }

        return Strings.padStart(taxId, size, '0');
    }

    public void calculateCid() {
        byte[] requestIdBytes = new byte[16];
        ByteBuffer.wrap(requestIdBytes)
            .putLong(requestId.getMostSignificantBits())
            .putLong(requestId.getLeastSignificantBits());

        var tradeName = (PersonType.LEGAL_ENTITY.equals(personType) && !Strings.isNullOrEmpty(fantasyName) ? fantasyName : "");

        final String entryAttributes = new StringBuilder()
            .append(type.keyTypeNameOnBacen()).append(SEPARATOR)
            .append(key).append(SEPARATOR)
            .append(taxId).append(SEPARATOR)
            .append(name).append(SEPARATOR)
            .append(tradeName).append(SEPARATOR)
            .append(ispb).append(SEPARATOR)
            .append(branchNumber).append(SEPARATOR)
            .append(accountNumber).append(SEPARATOR)
            .append(accountType.getInitials())
            .toString();

        HmacUtils hmacSHA256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, requestIdBytes);
        this.cid = hmacSHA256.hmacHex(entryAttributes.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    public String recalculateCid() {
        this.calculateCid();
        return this.cid;
    }

    public void keepCreationRequestIdentifier(final UUID requestId) {
        this.requestId = requestId;
    }

}
