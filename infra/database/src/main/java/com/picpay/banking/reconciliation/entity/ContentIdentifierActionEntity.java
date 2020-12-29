package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.ContentIdentifierFileAction;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
@Entity(name = "content_identifier_actions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ContentIdentifierActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Integer idContentIdentifierFile;

    @Column
    private Integer idVsyncHistory;

    @Column
    @Enumerated(EnumType.STRING)
    private ContentIdentifierFileAction action;

    @Column(name = "creation_date")
    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();

    @Type(type = "json")
    @Column(name = "content", columnDefinition = "json")
    private PixKeyEntity pixKey;

    @Column
    private String cid;

}
