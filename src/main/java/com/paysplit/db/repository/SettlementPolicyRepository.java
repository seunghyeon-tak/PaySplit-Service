package com.paysplit.db.repository;

import com.paysplit.db.domain.SettlementPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementPolicyRepository extends JpaRepository<SettlementPolicy, Long> {

}
