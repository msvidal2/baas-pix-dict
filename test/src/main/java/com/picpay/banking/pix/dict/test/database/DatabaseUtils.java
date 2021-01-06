package com.picpay.banking.pix.dict.test.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatabaseUtils {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void removeAll(final String keyType) {
        jdbcTemplate.update("delete from pix_key p where p.type = ?", keyType);
        jdbcTemplate.update("delete from sync_verifier s where s.key_type = ?", keyType);
        jdbcTemplate.update("delete from sync_verifier_historic_action s where s.id_sync_verifier_historic in " +
            "(select h.id from sync_verifier_historic h where h.key_type = ?)", keyType);
        jdbcTemplate.update("delete from sync_verifier_historic s where s.key_type = ?", keyType);
        jdbcTemplate.update("delete from content_identifier_event e where e.key_type = ?", keyType);
    }

    public String findVsyncResult(final String keyType) {
        var result = namedParameterJdbcTemplate.query(
            "select h.result " +
                "  from sync_verifier_historic h " +
                " where h.synchronized_end = (select max(l.synchronized_end) from sync_verifier_historic l where l.key_type = h.key_type) " +
                "   and h.key_type = :keyType",
            Map.of("keyType", keyType),
            (rs, rowNum) -> rs.getString("result"));

        return result.stream().findFirst().orElseThrow();
    }

}
