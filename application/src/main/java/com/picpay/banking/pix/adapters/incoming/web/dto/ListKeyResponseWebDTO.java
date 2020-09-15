package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class ListKeyResponseWebDTO {

    private String key;
    private String name;
    private String fantasyName;
    private LocalDateTime createdAt;
    private LocalDateTime startPossessionAt;
    private Integer claim;

    public static List<ListKeyResponseWebDTO> from(final Collection<PixKey> from) {
        return from.stream()
            .map(ListKeyResponseWebDTO::getPixKey)
            .collect(Collectors.toList());
    }

    private static ListKeyResponseWebDTO getPixKey(final PixKey pixKey) {
        return ListKeyResponseWebDTO.builder()
            .key(pixKey.getKey())
            .name(pixKey.getName())
            .fantasyName(pixKey.getFantasyName())
            .createdAt(pixKey.getCreatedAt())
            .startPossessionAt(pixKey.getStartPossessionAt())
            .claim(pixKey.getClaim() != null ? pixKey.getClaim().getValue() : null)
            .build();
    }

}
