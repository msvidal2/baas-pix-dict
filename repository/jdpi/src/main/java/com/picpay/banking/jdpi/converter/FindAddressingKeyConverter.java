package com.picpay.banking.jdpi.converter;

import com.picpay.banking.jdpi.dto.response.EstatisticasResponseDTO;
import com.picpay.banking.jdpi.dto.response.EventoResponseDTO;
import com.picpay.banking.jdpi.dto.response.FindAddressingKeyResponseDTO;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.Accountant;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.Aggregate;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.Statistic;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FindAddressingKeyConverter {

    public AddressingKey convert(final FindAddressingKeyResponseDTO from) {
        if (from == null) {
            return AddressingKey.builder().build();
        }

        return AddressingKey.builder()
            .type(                  (from.getTpChave() == null? null: KeyType.resolve(from.getTpChave())))
            .key(                   from.getChave())
            .ispb(                  from.getIspb())
            .nameIspb(              from.getNomeIspb())
            .branchNumber(          from.getNrAgencia())
            .accountNumber(         from.getNrConta())
            .accountType(           (from.getTpConta() == null? null: AccountType.resolve(from.getTpConta())))
            .accountOpeningDate(    from.getDtHrAberturaConta())
            .personType(            (from.getTpPessoa() == null? null: PersonType.resolve(from.getTpPessoa())))
            .cpfCnpj(               Long.valueOf(from.getCpfCnpj()).longValue())
            .name(                  from.getNome())
            .fantasyName(           from.getNomeFantasia())
            .createdAt(             from.getDtHrCriacaoChave())
            .startPossessionAt(     from.getDtHrInicioPosseChave())
            .endToEndId(            from.getEndToEndId())
            .statistic(             (from.getEstatisticas() == null? null: convertEstatisticas(from.getEstatisticas())))
            .build();
    }

    private Statistic convertEstatisticas(EstatisticasResponseDTO estatisticas) {

        if(estatisticas == null) {
            return null;
        }

        var accountants = new ArrayList<Accountant>();

        Accountant accountant = null;

        for (EventoResponseDTO eventoResponseDTO : estatisticas.getContadores()) {
            accountant = Accountant.builder()
                .aggregate(Aggregate.resolve(eventoResponseDTO.getAgregado()))
                .lastThreeDays(eventoResponseDTO.getD3())
                .lastThirtyDays(eventoResponseDTO.getD30())
                .lastSixMonths(eventoResponseDTO.getM6()).build();
            accountants.add(accountant);
        }

        return Statistic.builder().lastUpdateDateAntiFraud(estatisticas.getDtHrUltAtuAntiFraude())
            .accountants(accountants).build();
    }
}
