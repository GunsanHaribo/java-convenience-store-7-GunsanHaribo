package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class QuantityTest {
    @DisplayName("각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인 테스트")
    @ParameterizedTest
    @CsvSource(value = {"10:true", "11:false"}, delimiter = ':')
    void 각_상품의_재고_수량을_고려하여_결제_가능_여부를_확인_테스트(int requestQuantity, boolean expectedPossible) {
        Quantity quantity = new Quantity(10);
        boolean purchasable = quantity.isPurchasable(requestQuantity);

        assertThat(purchasable).isEqualTo(expectedPossible);
    }
}