package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    @DisplayName("프로모션 없는 상품내에 재고 감소 테스트입니다.")
    @Test
    void 프로모션_없는_상품내에_재고_감소_테스트입니다() {
        Product product = new Product("콜라", 1000, 10, "null");
        product.subtractQuantityWithoutPromotion(10);
        Quantity actualQuantity = product.getQuantity();

        assertThat(actualQuantity.getQuantity()).isEqualTo(0);
    }

    @DisplayName("프로모션 없는 상품내에 재고 감소 예외 테스트입니다.")
    @Test
    void 프로모션_없는_상품내에_재고_감소_예외_테스트입니다() {
        Product product = new Product("콜라", 1000, 10, "null");

        assertThatThrownBy(() -> product.subtractQuantityWithoutPromotion(11)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("프로모션이 있는 상품의 재고감소 테스트입니다.")
    @Test
    void 프로모션이_있는_상품의_재고감소_테스트() {
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        product.subtractQuantityWithPromotion(7);
        Quantity actualQuantity = product.getQuantity();

        assertThat(actualQuantity.getQuantity()).isEqualTo(0);
    }

    @DisplayName("프로모션이 있는 상품의 부족한 재고 반환 테스트입니다.")
    @Test
    void 프로모션이_있는_상품의_부족한_재고_반환_테스트() {
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        int lackOfQuantity = product.subtractQuantityWithPromotion(8);

        assertThat(lackOfQuantity).isEqualTo(2);
    }

    @DisplayName("프로모션이 있는 상품의 부족한 재고 반환 후 수량 확인 테스트입니다.")
    @Test
    void 프로모션이_있는_상품의_부족한_재고_반환_후_수량확인_테스트() {
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        product.subtractQuantityWithPromotion(8);
        int actualQuantity = product.getQuantity().getQuantity();

        assertThat(actualQuantity).isEqualTo(0);
    }

    @DisplayName("이름에 맞는 프로모션 반환하는 테스트 입니다.")
    @ParameterizedTest
    @MethodSource("providePromotionNameAndPromotion")
    void 이름에_맞는_프로모션_반환하는_테스트(String promotionName, Promotion promotion) {
        Product product = new Product("콜라", 1000, 10, promotionName);

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