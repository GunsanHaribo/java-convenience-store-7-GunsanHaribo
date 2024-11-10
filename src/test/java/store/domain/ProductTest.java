package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    private final Promotion cokePromotion = new Promotion("탄산2+1", 2, 1, "2024-01-01", "2024-12-31");

    @DisplayName("프로모션 없는 상품내에 재고 감소 테스트입니다.")
    @Test
    void 프로모션_없는_상품내에_재고_감소_테스트입니다() {
        Product product = new Product("콜라", 1000, 10, null);
        product.subtractQuantity(10);
        Quantity actualQuantity = product.getQuantity();

        assertThat(actualQuantity.getQuantity()).isEqualTo(0);
    }

    @DisplayName("프로모션 없는 상품내에 재고 감소 예외 테스트입니다.")
    @Test
    void 프로모션_없는_상품내에_재고_감소_예외_테스트입니다() {
        Product product = new Product("콜라", 1000, 10, null);

        assertThatThrownBy(() -> product.subtractQuantity(11)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("프로모션이 있는 상품의 재고감소 테스트입니다.")
    @Test
    void 프로모션이_있는_상품의_재고감소_테스트() {
        Product product = new Product("콜라", 1000, 10, cokePromotion);
        product.subtractQuantity(7);
        Quantity actualQuantity = product.getQuantity();

        assertThat(actualQuantity.getQuantity()).isEqualTo(3);
    }

    @DisplayName("이름에 맞는 프로모션 반환하는 테스트 입니다.")
    @Test
    void 이름에_맞는_프로모션_반환하는_테스트() {
        Promotions promotions = new Promotions("/promotions.md");
        Promotion promotionByName = promotions.findPromotionByName("탄산2+1");

        assertThat(promotionByName.isSameName("탄산2+1")).isTrue();
    }


    @DisplayName("혜택을 받을 수 있는 프로모션 수량을 계산")
    @ParameterizedTest
    @CsvSource(value = {"7:0", "8:1", "9:0", "11:0"}, delimiter = ':')
    void 혜택을_받을_수_있는_프로모션_수량을_계산(int requestQuantity, int expectedPromotionQuantity) {
        Product product = new Product("콜라", 1000, 10, cokePromotion);
        int actualPromotionQuantity = product.calculateRequiredPromotionQuantity(requestQuantity);

        assertThat(actualPromotionQuantity).isEqualTo(expectedPromotionQuantity);
    }

    @DisplayName("부족한 프로모션 재고 반환")
    @ParameterizedTest
    @CsvSource(value = {"9:0", "10:0", "11:1", "12:2"}, delimiter = ':')
    void 부족한_프로모션_재고_반환(int requestQuantity, int expectedLackOfQuantity) {
        Product product = new Product("콜라", 1000, 10, cokePromotion);
        int lackOfQuantity = product.checkLackOfPromotion(requestQuantity);

        assertThat(lackOfQuantity).isEqualTo(expectedLackOfQuantity);
    }
}