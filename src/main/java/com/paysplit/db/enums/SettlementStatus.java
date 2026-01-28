package com.paysplit.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SettlementStatus {
    READY("정산 준비") {
        @Override
        public boolean canTransitTo(SettlementStatus next) {
            return next == IN_PROGRESS || next == CANCELED;
        }
    },
    IN_PROGRESS("정산 진행 중") {
        @Override
        public boolean canTransitTo(SettlementStatus next) {
            return next == COMPLETED || next == FAILED;
        }
    },
    COMPLETED("정산 완료") {
        @Override
        public boolean canTransitTo(SettlementStatus next) {
            return false;
        }
    },
    FAILED("정산 실패") {
        @Override
        public boolean canTransitTo(SettlementStatus next) {
            return next == IN_PROGRESS || next == CANCELED;
        }
    },
    CANCELED("정산 취소") {
        @Override
        public boolean canTransitTo(SettlementStatus next) {
            return false;
        }
    };

    private final String description;

    public abstract boolean canTransitTo(SettlementStatus next);
}
