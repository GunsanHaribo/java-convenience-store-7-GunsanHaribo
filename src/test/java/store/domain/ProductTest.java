package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    private final Promotion cokePromotion = new Promotion("탄산2+1", 2, 1, "2024-01-01", "2024-12-31");
    private final Promotion managerPromotion = new Promotion("MD추천상품", 1, 1, "2024-01-01", "2024-12-31");

    @DisplayName("프로모션 없는 상품내에 재고 감소 테스트입니다.")
    @Test
    void 프로모션_없는_상품내에_재고_감소_테스트입니다() {
        Product product = new Product("콜라", 1000, 10, null);
        product.subtractQuantityWithoutPromotion(10);
        Quantity actualQuantity = product.getQuantity();

        assertThat(actualQuantity.getQuantity()).isEqualTo(0);
    }

    @DisplayName("프로모션 없는 상품내에 재고 감소 예외 테스트입니다.")
    @Test
    void 프로모션_없는_상품내에_재고_감소_예외_테스트입니다() {
        Product product = new Product("콜라", 1000, 10, null);

        assertThatThrownBy(() -> product.subtractQuantityWithoutPromotion(11)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("프로모션이 있는 상품의 재고감소 테스트입니다.")
    @Test
    void 프로모션이_있는_상품의_재고감소_테스트() {

        Product product = new Product("콜라", 1000, 10, cokePromotion);
        product.subtractQuantityWithPromotion(7, true);
        Quantity actualQuantity = product.getQuantity();

        assertThat(actualQuantity.getQuantity()).isEqualTo(3);
    }

    @DisplayName("프로모션이 있는 상품의 부족한 재고 반환 테스트입니다.")
    @ParameterizedTest
    @CsvSource(value = {"10:true:0", "12:true:2"}, delimiter = ':')
    void 프로모션이_있는_상품의_부족한_재고_반환_테스트(int requestQuantity, boolean isPromotionApplied, int expectedProductQuantity) {
        Product product = new Product("콜라", 1000, 10, cokePromotion);
        int lackOfQuantity = product.subtractQuantityWithPromotion(requestQuantity, isPromotionApplied);

        assertThat(lackOfQuantity).isEqualTo(expectedProductQuantity);
    }

    @DisplayName("프로모션이 있는 상품의 부족한 재고 반환 후 수량 확인 테스트입니다.")
    @ParameterizedTest
    @CsvSource(value = {"8:true:1", "8:false:2"}, delimiter = ':')
    void 프로모션이_있는_상품의_부족한_재고_반환_후_수량확인_테스트(int requestQuantity, boolean isPromotionApplied, int expectedProductQuantity) {
        Product product = new Product("콜라", 1000, 10, cokePromotion);
        product.subtractQuantityWithPromotion(requestQuantity, isPromotionApplied);
        int actualQuantity = product.getQuantity().getQuantity();

        assertThat(actualQuantity).isEqualTo(expectedProductQuantity);
    }

    @DisplayName("이름에 맞는 프로모션 반환하는 테스트 입니다.")
    @Test
    void 이름에_맞는_프로모션_반환하는_테스트() {
        Promotions promotions = new Promotions("/promotions.md");
        Promotion promotionByName = promotions.findPromotionByName("탄산2+1");

        assertThat(promotionByName.isSameName("탄산2+1")).isTrue();
    }

    @DisplayName("프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 혜택적용 가능한 수량 반환 테스트")
    @ParameterizedTest
    @CsvSource(value = {"9:true:0", "9:false:1"}, delimiter = ':')
    void 프로모션이_적용_가능한_상품의_경우_수량_보다_적게_가져왔고_아직_프로모션_재고가_남아_있을떄_혜택적용_가능한_수량_반환(int requestQuantity, boolean isPromotionApplied, int expectedProductQuantity) {
        Product product = new Product("초코송이", 1000, 10, managerPromotion);
        product.subtractQuantityWithPromotion(requestQuantity, isPromotionApplied);

        assertThat(product.getQuantity().getQuantity()).isEqualTo(expectedProductQuantity);
    }

    @DisplayName("재고 한도 내에서 프로모션 수량을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"7:0", "8:1", "9:0", "11:0"}, delimiter = ':')
    void 재고_한도_내에서_프로모션_수량을_계산(int requestQuantity, int expectedPromotionQuantity) {
        Product product = new Product("콜라", 1000, 10, cokePromotion);
        int actualPromotionQuantity = product.calculatePromotionQuantity(requestQuantity);

        assertThat(actualPromotionQuantity).isEqualTo(expectedPromotionQuantity);
    }
}