package store.service;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.application.ProductDto;
import store.application.ProductsDto;
import store.application.PromotionPurchasingResultDto;
import store.domain.PromotionPurchaseStatus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.AbstractMap;
import java.util.Map;

import static store.domain.PromotionPurchaseStatus.PROMOTION_NOT_EXIST;

class ConvenienceStoreServiceTest {
    @DisplayName("현재 재고 반환")
    @ParameterizedTest
    @CsvSource(value = {"0:콜라:1000:10:탄산2+1", "1:콜라:1000:10:''"}, delimiter = ':')
    void 현재_재고_반환_테스트(int index, String name, int price, int quantity, String promotion) {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService();
        ProductsDto productsDto = convenienceStoreService.loadInitialProducts();
        ProductDto product = productsDto.getProducts().get(index);


        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(product.getName()).isEqualTo(name);
            softly.assertThat(product.getPrice()).isEqualTo(price);
            softly.assertThat(product.getQuantity()).isEqualTo(quantity);
            softly.assertThat(product.getPromotion()).isEqualTo(promotion);
        });
    }

    @DisplayName("프로모션_상품_구매_테스트")
    @ParameterizedTest
    @CsvSource(value = {"콜라:11:LACK_OF_PROMOTION_QUANTITY:1", "오렌지주스:10:LACK_OF_PROMOTION_QUANTITY:1", "콜라:8:PROMOTION_CAN_BE_APPLIED:1"}, delimiter = ':')
    void 프로모션_상품_구매_테스트(String productName, int productQuantity, PromotionPurchaseStatus promotionPurchaseStatus, int expectedResultQuantity) {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService();
        Map.Entry<String, Integer> requestProduct = new AbstractMap.SimpleEntry<>(productName, productQuantity);
        PromotionPurchasingResultDto promotionPurchasingResultDto = convenienceStoreService.purchasePromotionProduct(requestProduct);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(promotionPurchasingResultDto.getStatus()).isEqualTo(promotionPurchaseStatus);
            softly.assertThat(promotionPurchasingResultDto.getQuantity()).isEqualTo(expectedResultQuantity);
        });
    }

    @DisplayName("프로모션 상품 구매시 프로모션이 없을떄 테스트")
    @Test
    void 프로모션_상품_구매시_프로모션이_없을떄_테스트() {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService();
        Map.Entry<String, Integer> requestProduct = new AbstractMap.SimpleEntry<>("물", 3);
        PromotionPurchasingResultDto promotionPurchasingResultDto = convenienceStoreService.purchasePromotionProduct(requestProduct);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(promotionPurchasingResultDto.getStatus()).isEqualTo(PROMOTION_NOT_EXIST);
            softly.assertThat(promotionPurchasingResultDto.getQuantity()).isEqualTo(3);
        });
    }

    @DisplayName("프로모션이 없을 상품 차감 테스트")
    @Test
    void 프로모션이_없을_상품_차감_테스트() {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService();
        Map.Entry<String, Integer> requestProduct = new AbstractMap.SimpleEntry<>("비타민워터", 7);

        assertThatThrownBy(() -> convenienceStoreService.purchaseNoPromotionProducts(requestProduct)).isInstanceOf(IllegalArgumentException.class);
    }
}