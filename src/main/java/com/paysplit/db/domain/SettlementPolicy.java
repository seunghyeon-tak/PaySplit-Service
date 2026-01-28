package com.paysplit.db.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "settlement_policies")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class SettlementPolicy {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id", unique = true)
    private Settlement settlement;

    @Column(name = "platform_fee_rate", precision = 5, scale = 4, nullable = false)
    private BigDecimal platformFeeRate;

    @Column(name = "leader_share_rate", precision = 5, scale = 4, nullable = false)
    private BigDecimal leaderShareRate;

    @Column(name = "policy_version", length = 20)
    private String policyVersion;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
