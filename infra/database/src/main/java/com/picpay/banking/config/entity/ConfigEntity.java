package com.picpay.banking.config.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "config")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfigEntity {

    @Id
    private String key;

    private String value;

}
