package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.domain.Promotion.findPromotionByName;

class ProductTest {
    @DisplayName("상품내에 재고 감소 테스트입니다.")
    @Test
    void 상품내에_재고_감소_테스트입니다() {
        Product product = new Product(10, "탄산2+1");
        product.subtractQuantity(10);
        Quantity actualQuantity = product.getQuantity();

        assertThat(actualQuantity.getQuantity()).isEqualTo(0);
    }

    @DisplayName("상품내에 재고 감소 예외 테스트입니다.")
    @Test
    void 상품내에_재고_감소_예외_테스트입니다() {
        Product product = new Product(10, "탄산2+1");

        assertThatThrownBy(() -> product.subtractQuantity(11)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름에 맞는 프로모션 반환하는 테스트 입니다.")
    @ParameterizedTest
    @MethodSource("providePromotionNameAndPromotion")
    void 이름에_맞는_프로모션_반환하는_테스트(String promotionName, Promotion promotion) {
        Product product = new Product(10, promotionName);

        assertThat(product.getPromotion()).isEqualTo(promotion);
    }

    static Stream<Arguments> providePromotionNameAndPromotion() {
        return Stream.of(
                Arguments.of("탄산2+1", Promotion.SORT_DRINK_TWO_PLUS_ONE),
                Arguments.of("MD추천상품", Promotion.MANAGER_RECOMMENDATION),
                Arguments.of("반짝할인", Promotion.FLASH_SALE),
                Arguments.of("null", Promotion.NONE),
                Arguments.of("hi", Promotion.NONE)
        );
    }
}