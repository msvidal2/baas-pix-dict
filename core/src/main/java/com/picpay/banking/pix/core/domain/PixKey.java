package com.picpay.banking.pix.core.domain;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.ObjectUtils;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.google.common.base.Charsets.UTF_8;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
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
    private LocalDateTime startPossessionAt;
    private String endToEndId;
    private String correlationId;
    private ClaimType claim;
    private Statistic statistic;
    private String cid;

    private UUID requestId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PixKey pixKey = (PixKey) o;
        return type == pixKey.type &&
            key.equals(pixKey.key) &&
            ispb.equals(pixKey.ispb) &&
            branchNumber.equals(pixKey.branchNumber) &&
            accountType == pixKey.accountType &&
            accountNumber.equals(pixKey.accountNumber) &&
            personType == pixKey.personType &&
            taxId.equals(pixKey.taxId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, key, ispb, branchNumber, accountType, accountNumber, personType, taxId);
    }

    public String calculateCid() {

        byte[] requestIdBytes = new byte[16];

        ByteBuffer.wrap(requestIdBytes)
            .putLong(requestId.getMostSignificantBits())
            .putLong(requestId.getLeastSignificantBits());

        return Hashing.hmacSha256(requestIdBytes).newHasher()
            .putString(type.name(), UTF_8).putString(SEPARATOR, UTF_8)
            .putString(key, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(taxId, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(name, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(fantasyName == null ? "" : fantasyName, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(String.valueOf(ispb), UTF_8).putString(SEPARATOR, UTF_8)
            .putString(branchNumber == null ? "" : branchNumber, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(accountNumber, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(accountType.getInitials(), UTF_8)
            .hash().toString()
            .toLowerCase();
    }

}
