package com.picpay.banking.pix.dict.test.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatabaseUtils {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void removeAll(final String keyType) {
        jdbcTemplate.update("delete from dict.pix_key p where p.type = ?", keyType);
        jdbcTemplate.update("delete from dict.sync_verifier s where s.key_type = ?", keyType);
        jdbcTemplate.update("delete from dict.content_identifier_event e where e.key_type = ?", keyType);
    }

    public String findVsync(final String keyType) {
        var result = namedParameterJdbcTemplate.query(
            "select s.vsync from dict.sync_verifier s where s.key_type = :keyType",
            Map.of("keyType", keyType),
            (rs, rowNum) -> rs.getString("vsync"));

        return result.stream().findFirst().orElseThrow();
    }

    public String findVsyncResult(final String keyType) {
        var result = namedParameterJdbcTemplate.query(
            "select h.result " +
                "  from dict.sync_verifier_historic h " +
                " where h.synchronized_end = (select max(l.synchronized_end) from dict.sync_verifier_historic l where l.key_type = h.key_type) " +
                "   and h.key_type = :keyType",
            Map.of("keyType", keyType),
            (rs, rowNum) -> rs.getString("result"));

        return result.stream().findFirst().orElseThrow();
    }

    public String findAnyPixKeyByType(final String keyType) {
        return namedParameterJdbcTemplate.query(
            "select k.key from dict.pix_key k where k.type = :keyType limit 1",
            Map.of("keyType", keyType),
            (rs, rowNum) -> rs.getString("")).get(0);
    }

    public List<String> findCidEventsByPixKey(final String pixKey) {
        return namedParameterJdbcTemplate.query(
            "select e.type from dict.content_identifier_event e where e.pix_key = :pixKey",
            Map.of("pixKey", pixKey),
            (rs, rowNum) -> rs.getString(""));
    }

}
