package com.picpay.banking.claim.repository.specifications;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor
public class ListClaims implements Specification<ClaimEntity> {

    private static final long serialVersionUID = 4905817650008280685L;

    private final Claim claim;

    private final Boolean isClaimer;
    private final Boolean isPending;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        var predicates = new ArrayList<Predicate>();

        if(!Objects.isNull(isClaimer) && isClaimer) {
            predicates.add(criteriaBuilder.equal(root.get("claimerParticipant"), claim.getIspb()));
            if(!Objects.isNull(claim.getPersonType())) predicates.add(criteriaBuilder.equal(root.get("claimerType"), claim.getPersonType()));
            if(!Objects.isNull(claim.getCpfCnpj())) predicates.add(criteriaBuilder.equal(root.get("claimerTaxId"), claim.getCpfCnpj()));
            if(!Objects.isNull(claim.getBranchNumber())) predicates.add(criteriaBuilder.equal(root.get("claimerBranch"), claim.getBranchNumber()));
            if(!Objects.isNull(claim.getAccountNumber())) predicates.add(criteriaBuilder.equal(root.get("claimerAccountNumber"), claim.getAccountNumber()));
            if(!Objects.isNull(claim.getAccountType())) predicates.add(criteriaBuilder.equal(root.get("claimerAccountType"), claim.getAccountType()));
        } else {
            var keyPath = root.get("pixKey");

            predicates.add(criteriaBuilder.equal(root.get("donorParticipant"), claim.getIspb()));
            if(!Objects.isNull(claim.getPersonType())) predicates.add(criteriaBuilder.equal(keyPath.get("personType"), claim.getPersonType()));
            if(!Objects.isNull(claim.getCpfCnpj())) predicates.add(criteriaBuilder.equal(keyPath.get("taxId"), claim.getCpfCnpj()));
            if(!Objects.isNull(claim.getBranchNumber())) predicates.add(criteriaBuilder.equal(keyPath.get("branch"), claim.getBranchNumber()));
            if(!Objects.isNull(claim.getAccountNumber())) predicates.add(criteriaBuilder.equal(keyPath.get("accountNumber"), claim.getAccountNumber()));
            if(!Objects.isNull(claim.getAccountType())) predicates.add(criteriaBuilder.equal(keyPath.get("accountType"), claim.getAccountType()));
        }

        predicates.add(criteriaBuilder.between(root.get("lastModified"), startDate, endDate));

        if(isPending){
            predicates.add(root.get("status").in(ClaimSituation.getPending()));
        } else {
            predicates.add(root.get("status").in(ClaimSituation.getNotPending()));
        }

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }

}
