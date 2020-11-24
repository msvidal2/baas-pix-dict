package com.picpay.banking.fallbacks;

import com.picpay.banking.fallbacks.dto.Violation;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.Optional;

public class PixKeyFieldResolver implements FieldResolver {

    private final Map<String, String> fields = Map.of(
            "entry.key", "key",
            "entry.keytype", "type",
            "entry.account.participant", "ispb",
            "entry.account.branch", "branchNumber",
            "entry.account.accountnumber", "accountNumber",
            "entry.account.accounttype", "accountType",
            "entry.account.openingdate", "accountOpeningDate",
            "entry.owner.type", "personType",
            "entry.owner.taxidnumber", "taxId",
            "entry.owner.name", "name"
    );

    @Override
    public FieldError resolve(Violation violation) {
        var field = fields.get(violation.getProperty().toLowerCase());

        return new FieldError(violation.getValue(),
                Optional.ofNullable(field).orElse(violation.getProperty()),
                violation.getReason());
    }

}
