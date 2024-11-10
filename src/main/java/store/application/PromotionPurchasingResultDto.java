package store.application;

import store.domain.PromotionPurchaseStatus;

public class PromotionPurchasingResultDto {
    private final PromotionPurchaseStatus status;
    private final int quantity;

    public PromotionPurchasingResultDto(PromotionPurchaseStatus status, int quantity) {
        this.status = status;
        this.quantity = quantity;
    }

    public PromotionPurchaseStatus getStatus() {
        return status;
    }

    public int getQuantity() {
        return quantity;
    }
}
