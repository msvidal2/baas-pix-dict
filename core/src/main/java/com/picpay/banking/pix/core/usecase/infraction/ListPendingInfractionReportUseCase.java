package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class ListPendingInfractionReportUseCase {

//    private InfractionReportPort infractionReportPort;

    public List<InfractionReport> execute(@NonNull final Integer ispb, final Integer limite) {
        //TODO ajustar com nova porta -> será feito no contexto do sincronizador
//        List<InfractionReport> infractionsPending = infractionReportPort.listPending(ispb, limite);
//
//        if (infractionsPending != null)
//            log.info("Infraction_listedPending", kv("endToEndId", infractionsPending.size()));
//
//        return infractionsPending;
        return null;
    }

}
