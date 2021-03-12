package com.picpay.banking.pixkey.entity;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.PixKeyEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "pix_key_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PixKeyEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PixKeyEvent type;

    @Column(nullable = false)
    private String requestIdentifier;

    private String pixKey;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private KeyType pixKeyType;

    @Type(type = "json")
    @Column(name = "event_data", columnDefinition = "json", nullable = false)
    private PixKey data;

    @CreatedDate
    private LocalDateTime creationDate;

}
