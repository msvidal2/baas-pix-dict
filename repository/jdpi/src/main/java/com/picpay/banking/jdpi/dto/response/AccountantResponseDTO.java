package com.picpay.banking.jdpi.dto.response;

import com.picpay.banking.pix.core.domain.AccountantType;
import com.picpay.banking.pix.core.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class AccountantResponseDTO {

    private AccountantType type;
    private Aggregate aggregate;
    private int lastThreeDays;
    private int lastThirtyDays;
    private int lastSixMonths;

    public AccountantResponseDTO(EventoResponseDTO evento){
        this.type = AccountantType.valueOf(String.valueOf(evento.getTipo()));
        this.aggregate = Aggregate.valueOf(String.valueOf(evento.getAgregado()));
        this.lastThreeDays = evento.getD3();
        this.lastThirtyDays = evento.getD30();
        this.lastSixMonths = evento.getM6();
    }
}
