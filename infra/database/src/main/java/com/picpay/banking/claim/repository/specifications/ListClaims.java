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

        predicates.add(criteriaBuilder.equal(root.get("endDate"), endDate));
        predicates.add(criteriaBuilder.equal(root.get("startDate"), startDate));

        if(isClaimer) {
            predicates.add(criteriaBuilder.equal(root.get("claimerParticipant"), claim.getIspb()));
            predicates.add(criteriaBuilder.equal(root.get("claimerType"), claim.getPersonType()));
            predicates.add(criteriaBuilder.equal(root.get("claimerTaxId"), claim.getCpfCnpj()));
            predicates.add(criteriaBuilder.equal(root.get("claimerBranch"), claim.getBranchNumber()));
            predicates.add(criteriaBuilder.equal(root.get("claimerAccountNumber"), claim.getAccountNumber()));
            predicates.add(criteriaBuilder.equal(root.get("claimerAccountType"), claim.getAccountType()));
        } else {
            predicates.add(criteriaBuilder.equal(root.get("donorParticipant"), claim.getDonorIspb()));
            predicates.add(criteriaBuilder.equal(root.get("personType"), claim.getDonorData().getPersonType()));
            predicates.add(criteriaBuilder.equal(root.get("cpfCnpj"), claim.getDonorData().getCpfCnpj()));
            predicates.add(criteriaBuilder.equal(root.get("branchNumber"), claim.getDonorData().getBranchNumber()));
            predicates.add(criteriaBuilder.equal(root.get("accountNumber"), claim.getDonorData().getAccountNumber()));
            predicates.add(criteriaBuilder.equal(root.get("accountType"), claim.getDonorData().getAccountType()));
        }

        if(!Objects.isNull(isPending) && isPending) {
            CriteriaBuilder.In<ClaimSituation> claimSituation = criteriaBuilder.in(root.get("claimSituation"));
            ClaimSituation.getPending().forEach(claimSituation::value);
        }

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }

}
