package com.picpay.banking.pixkey.entity;

import com.picpay.banking.pix.core.domain.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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

    @Column(name = "pix_key")
    private String key;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private KeyType type;

}
