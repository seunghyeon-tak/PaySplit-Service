package com.paysplit.api.controller;

import com.paysplit.api.business.SettlementExecuteBusiness;
import com.paysplit.api.dto.settlement.request.SettlementExecuteRequest;
import com.paysplit.api.dto.settlement.response.SettlementExecuteResponse;
import com.paysplit.api.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/settlements")
@RequiredArgsConstructor
public class SettlementExecuteController {
    private final SettlementExecuteBusiness settlementExecuteBusiness;

    @PostMapping
    public ApiResponse<SettlementExecuteResponse> create(@Valid @RequestBody SettlementExecuteRequest request) {
        SettlementExecuteResponse response = settlementExecuteBusiness.execute(request);

        return ApiResponse.success(response);
    }
}
