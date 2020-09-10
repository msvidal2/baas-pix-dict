package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.InfractionJDClient;
import com.picpay.banking.jdpi.dto.response.ListPendingInfractionReportDTO;
import com.picpay.banking.jdpi.dto.response.PendingInfractionReportDTO;
import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfractionPortImplTest {

    private InfractionReportPort port;

    @Mock
    private InfractionJDClient jdClient;

    private ListPendingInfractionReportDTO listPendingInfractionReportDTO;

    @BeforeEach
    public void init(){

        this.port = new InfractionReportPortImpl(jdClient);

        PendingInfractionReportDTO infractionReport = PendingInfractionReportDTO.builder().detalhes("details").dtHrCriacaoRelatoInfracao("10/04/2020").dtHrUltModificacao("10/04/2020")
            .idRelatoInfracao(randomUUID().toString())
            .endToEndId("ID_END_TO_END").ispbCreditado(1).ispbDebitado(2).reportadoPor(ReportedBy.CREDITED_PARTICIPANT.getValue())
            .stRelatoInfracao(InfractionReportSituation.OPEN.getValue())
            .tpInfracao(InfractionType.FRAUD.getValue())
            .detalhesAnalise("details")
            .resultadoAnalise(InfractionAnalyzeResult.ACCEPTED.getValue())
            .build();

        this.listPendingInfractionReportDTO = ListPendingInfractionReportDTO.builder().dtHrJdPi("10/04/2020 22:22:22")
            .temMaisElementos(true)
            .reporteInfracao(List.of(infractionReport))
            .build();
    }

    @Test
    void when_listInfractions_expect_ok() {

        when(jdClient.listPendings(anyInt(),anyInt()))
            .thenReturn(listPendingInfractionReportDTO);

        Pagination<InfractionReport> pagination = this.port.listPendingInfractionReport(1, 1);
        assertThat(pagination.getResult()).isNotEmpty();
        assertThat(pagination.getHasNext()).isTrue();

        verify(jdClient).listPendings(anyInt(),anyInt());
    }

}