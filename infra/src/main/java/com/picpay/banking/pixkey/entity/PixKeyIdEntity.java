package com.picpay.banking.pixkey.entity;

import com.picpay.banking.pixkey.dto.request.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PixKeyIdEntity implements Serializable {

    private String key;

    @Enumerated(EnumType.STRING)
    private KeyType type;

    private String taxId;

}
