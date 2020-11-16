package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.jdpi.dto.response.JDFieldErroDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldErrorDTO {

    @ApiModelProperty(value = "Field")
    private String field;

    @ApiModelProperty(value = "Message")
    private String message;

    public static FieldErrorDTO from(FieldError fieldError) {
        return new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage());
    }

    public static FieldErrorDTO from(final JDFieldErroDTO fieldErro) {
        return FieldErrorDTO.builder()
                .field(fieldErro.getField())
                .message(String.join(",", fieldErro.getMessages()))
                .build();
    }

}