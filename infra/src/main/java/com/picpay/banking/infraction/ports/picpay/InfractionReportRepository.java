/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.ports.picpay;

import com.picpay.banking.infraction.entity.InfractionReportEntity;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Repository
public interface InfractionReportRepository extends JpaRepository<InfractionReportEntity, String> {

    @Query("SELECT ir FROM InfractionReportEntity ir WHERE ir.ispbRequester = :ispb AND (:situation is null or ir.situation = :situation)" +
        " AND (ir.lastUpdateDate is null or ir.lastUpdateDate >= :dateStart) AND (cast(:dateEnd as timestamp) is null or ir.lastUpdateDate >= :dateEnd)")
    List<InfractionReportEntity> list(@Param("ispb") Integer ispb, @Param("situation") InfractionReportSituation situation,
        @Param("dateStart") LocalDateTime dateStart, @Param("dateEnd") LocalDateTime dateEnd);

}
