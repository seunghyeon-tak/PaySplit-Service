package com.paysplit.db.repository;

import com.paysplit.db.domain.BillingKey;
import com.paysplit.db.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingKeyRepository extends JpaRepository<BillingKey, Long> {
    Optional<BillingKey> findByUser(User user);
}
