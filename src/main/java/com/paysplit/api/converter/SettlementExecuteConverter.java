package com.paysplit.api.converter;

import com.paysplit.api.dto.settlement.request.SettlementExecuteRequest;
import com.paysplit.common.annotation.Converter;
import com.paysplit.db.domain.Settlement;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class SettlementExecuteConverter {
    public Settlement toEntity(SettlementExecuteRequest request) {
        // settlement entity builder 하기
        return null;
    }

    public void toResponse() {
        // 일단 반환값을 void로 했지만, 반환 dto가 추가로 필요할것 같음
    }
}
