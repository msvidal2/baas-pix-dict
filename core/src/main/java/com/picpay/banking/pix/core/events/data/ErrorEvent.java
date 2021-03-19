package com.picpay.banking.pix.core.events.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@Data
@JsonInclude(NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEvent {

    private String code;
    private String description;

}
