package com.picpay.banking.fallbacks;

import java.util.Map;

public class PixKeyFieldResolver implements FieldResolver {

    @Override
    public Map<String, String> fieldsMap() {
        return Map.of(
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
    }

}
