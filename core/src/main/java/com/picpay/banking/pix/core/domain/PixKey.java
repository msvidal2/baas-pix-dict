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

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@EqualsAndHashCode(of = {"type", "key", "ispb", "branchNumber", "accountType", "accountNumber", "personType", "taxId"})
public class PixKey implements Serializable {

    private static final long serialVersionUID = -6450851575119416890L;

    private static final String SEPARATOR = "&";
    public static final int CID_BYTES_SIZE = 16;
    public static final int CPF_SIZE = 11;
    public static final int CNPJ_SIZE = 14;

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

    public String getName() {
        return ObjectUtils.firstNonNull(name, fantasyName);
    }

    public String getTaxIdWithLeftZeros() {
        int size = CPF_SIZE;

        if (PersonType.LEGAL_ENTITY == personType) {
            size = CNPJ_SIZE;
        }

        return Strings.padStart(taxId, size, '0');
    }

    public void calculateCid() {
        byte[] requestIdBytes = new byte[CID_BYTES_SIZE];
        ByteBuffer.wrap(requestIdBytes)
            .putLong(requestId.getMostSignificantBits())
            .putLong(requestId.getLeastSignificantBits());

        final String entryAttributes = new StringBuilder()
            .append(type.keyTypeNameOnBacen()).append(SEPARATOR)
            .append(key).append(SEPARATOR)
            .append(taxId).append(SEPARATOR)
            .append(name).append(SEPARATOR)
            .append(Optional
                    .ofNullable(fantasyName)
                    .orElse(""))
                .append(SEPARATOR)
            .append(ispb).append(SEPARATOR)
            .append(branchNumber).append(SEPARATOR)
            .append(accountNumber).append(SEPARATOR)
            .append(accountType.getInitials())
            .toString();

        HmacUtils hmacSHA256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, requestIdBytes);
        this.cid = hmacSHA256.hmacHex(entryAttributes.getBytes(StandardCharsets.UTF_8)).toLowerCase(Locale.getDefault());
    }

    public String recalculateCid() {
        this.calculateCid();
        return this.cid;
    }

    public void keepCreationRequestIdentifier(final UUID requestId) {
        this.requestId = requestId;
    }

}
