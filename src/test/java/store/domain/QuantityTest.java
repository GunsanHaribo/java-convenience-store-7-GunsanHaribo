package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class QuantityTest {
    @DisplayName("각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인 한다.")
    @ParameterizedTest
    @CsvSource(value = {"10:true", "11:false"}, delimiter = ':')
    void 각_상품의_재고_수량을_고려하여_결제_가능_여부를_확인_테스트(int requestQuantity, boolean expectedPossible) {
        Quantity quantity = new Quantity(10);
        boolean purchasable = quantity.isPurchasable(requestQuantity);

        assertThat(purchasable).isEqualTo(expectedPossible);
    }

    @DisplayName("결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.")
    @Test
    void 결제된_수량만큼_해당_상품의_재고에서_차감하여_수량을_관리한다() {
        Quantity quantity = new Quantity(10);
        Quantity subtractedQuantity = quantity.subtractQuantity(10);

        assertThat(subtractedQuantity.getQuantity()).isEqualTo(0);
    }

    @DisplayName("결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리 예외 테스트")
    @Test
    void 결제된_수량만큼_해당_상품의_재고에서_차감하여_수량을_관리_예외_테스트() {
        Quantity quantity = new Quantity(10);

        assertThatThrownBy(() -> quantity.subtractQuantity(11)).isInstanceOf(IllegalArgumentException.class);
    }
}