package com.picpay.banking.pixkey.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "pix_key_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PixKeyEvent {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private KeyEventType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
            @JoinColumn(name = "pix_key_fk", referencedColumnName = "pix_key"),
            @JoinColumn(name = "key_type_fk", referencedColumnName = "type") })
    private PixKeyEntity pixKey;

    @Type(type = "json")
    @Column(name = "event_data", columnDefinition = "json", nullable = false)
    private PixKeyEntity data;

    @CreatedDate
    private LocalDateTime creationDate;

}
