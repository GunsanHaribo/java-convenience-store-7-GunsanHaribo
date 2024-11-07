package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    @DisplayName("상품내에 재고 감소 테스트입니다.")
    @Test
    void 상품내에_재고_감소_테스트입니다() {
        Product product = new Product(10);
        product.subtractQuantity(10);
        Quantity actualQuantity = product.getQuantity();

        assertThat(actualQuantity.getQuantity()).isEqualTo(0);
    }

    @DisplayName("상품내에 재고 감소 예외 테스트입니다.")
    @Test
    void 상품내에_재고_감소_예외_테스트입니다() {
        Product product = new Product(10);

        assertThatThrownBy(() -> product.subtractQuantity(11)).isInstanceOf(IllegalArgumentException.class);
    }
}