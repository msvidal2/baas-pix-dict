package com.picpay.banking.pixkey.repository.specifications;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor
public class ListKeys implements Specification<PixKeyEntity> {

    private static final long serialVersionUID = 4905817650008280685L;

    private final PixKey pixKey;

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        var predicates = new ArrayList<Predicate>();
        predicates.add(criteriaBuilder.equal(root.get("taxId"), pixKey.getTaxId()));
        predicates.add(criteriaBuilder.equal(root.get("personType"), pixKey.getPersonType()));
        predicates.add(criteriaBuilder.equal(root.get("accountNumber"), pixKey.getAccountNumber()));
        predicates.add(criteriaBuilder.equal(root.get("accountType"), pixKey.getAccountType()));
        predicates.add(criteriaBuilder.equal(root.get("participant"), pixKey.getIspb()));

        if(!Objects.isNull(pixKey.getBranchNumber())) {
            predicates.add(criteriaBuilder.equal(root.get("branch"), pixKey.getBranchNumber()));
        }

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }

}
