package com.paysplit.api.dto.billing.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingKeyChargeRequest {
    @Schema(name = "customer_key", description = "구매자 ID", example = "aENcQAtPdYbTjGhtQnNVj")
    @NotNull
    private String customerKey;

    @Schema(description = "결제 금액", example = "10000")
    @NotNull
    private Integer amount;

    @Schema(name = "order_id", description = "주문 ID", example = "a4CWyWY5m89PNh7xJwhk1")
    @NotNull
    private Long orderId;

    @Schema(name = "order_name", description = "주문명", example = "토스 프라임 구독")
    @NotNull
    private String orderName;

    @Schema(name = "customer_email", description = "구매자 이메일", example = "test@example.com")
    private String customerEmail;

    @Schema(name = "customer_name", description = "구매자 이름", example = "김아무개")
    private String customerName;
}
