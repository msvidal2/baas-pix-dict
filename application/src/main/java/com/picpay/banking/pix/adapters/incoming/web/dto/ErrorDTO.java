package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PRIVATE;

@JsonInclude(NON_NULL)
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
public class ErrorDTO {

    @ApiModelProperty(value = "Code Status")
    private int code;

    @ApiModelProperty(value = "Error")
    private String error;

    @ApiModelProperty(value = "API Error Code")
    private String apiErrorCode;

    @ApiModelProperty(value = "Message error")
    private String message;

    @ApiModelProperty(value = "Field Message")
    private List<FieldErrorDTO> fieldErrors;

    @ApiModelProperty(value = "Datetime event message")
    private LocalDateTime timestamp;

    public static ErrorDTO from(final HttpStatus status, final String message, final List<FieldErrorDTO> fieldErrors, final String apiErrorCode) {
        return ErrorDTO.builder()
                .code(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .fieldErrors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .apiErrorCode(apiErrorCode)
                .build();
    }

    public static ErrorDTO from(final HttpStatus status, final String message, final List<FieldErrorDTO> fieldErrors) {
        return from(status, message, fieldErrors, null);
    }

    public static ErrorDTO from(final HttpStatus status, final String message) {
        return from(status, message, null, null);
    }

}