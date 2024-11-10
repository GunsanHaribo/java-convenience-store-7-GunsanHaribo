package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReceiptTest {
    @DisplayName("프로모션 혜택을 제외한 금액 계산")
    @Test
    void 프로모션_혜택을_제외한_금액_계산() {
        Receipt receipt = new Receipt();
        receipt.updateFullPriceProduct(new PurchasedProduct("콜라", 5,1000));

        assertThat(receipt.calculateFullPrice()).isEqualTo(5000);
    }

    @DisplayName("프로모션 할인 금액을 계산")
    @Test
    void 프로모션_할인_금액을_계산() {
        Receipt receipt = new Receipt();
        receipt.updateFreeProduct(new PurchasedProduct("물", 5,1000));

        assertThat(receipt.calculatePromotionDiscount()).isEqualTo(5000);
    }
}