package store.service;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.application.ProductDto;
import store.application.ProductsDto;
import store.application.PromotionPurchasingResultDto;
import store.application.ReceiptDto;
import store.domain.PromotionPurchaseStatus;

import java.util.AbstractMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.domain.PromotionPurchaseStatus.PROMOTION_NOT_EXIST;

class ConvenienceStoreServiceTest {
    @DisplayName("현재 재고 반환")
    @ParameterizedTest
    @CsvSource(value = {"콜라:1000:10:탄산2+1"}, delimiter = ':')
    void 현재_재고_반환_테스트(String name, int price, int quantity, String promotion) {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService();
        ProductsDto productsDto = convenienceStoreService.loadInitialProducts();
        ProductDto productDto = productsDto.getProducts().get(name).get(0);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(productDto.getPrice()).isEqualTo(price);
            softly.assertThat(productDto.getQuantity()).isEqualTo(quantity);
            softly.assertThat(productDto.getPromotion()).isEqualTo(promotion);
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

    @DisplayName("프로모션 아닌 상품 구매 후 현재 프로모션재고 갯수확인 테스트")
    @Test
    void 프로모션_아닌_생품_구매_후_현재_프로모션재고_갯수확인_테스트() {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService();
        Map.Entry<String, Integer> requestProduct1 = new AbstractMap.SimpleEntry<>("비타민워터", 6);
        convenienceStoreService.purchaseNoPromotionProducts(requestProduct1);

        ProductsDto productsDto = convenienceStoreService.loadInitialProducts();
        int quantityVitamin = productsDto.getProducts().get("비타민워터").get(0).getQuantity();

        assertThat(quantityVitamin).isEqualTo(0);
    }

    @DisplayName("프로모션 구매 후 현재 프로모션재고 갯수확인 테스트")
    @ParameterizedTest
    @CsvSource(value = {"콜라:8:탄산2+1:2", "콜라:9:탄산2+1:1", "콜라:10:탄산2+1:0", "콜라:11:탄산2+1:0"}, delimiter = ':')
    void 프로모션_구매_후_현재_프로모션재고_갯수확인_테스트(String productName, int requestQuantity, String promotionName, int expectQuantity) {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService();
        Map.Entry<String, Integer> requestProduct1 = new AbstractMap.SimpleEntry<>(productName, requestQuantity);
        convenienceStoreService.purchasePromotionProduct(requestProduct1);

        ProductsDto productsDto = convenienceStoreService.loadInitialProducts();
        int quantityVitamin = productsDto.getProducts().get(productName).stream()
                .filter(product -> promotionName.equals(product.getPromotion()))
                .mapToInt(ProductDto::getQuantity)
                .findFirst()
                .orElse(-1);

        assertThat(quantityVitamin).isEqualTo(expectQuantity);
    }

    @DisplayName("영수증 구매 금액 계산 테스트")
    @Test
    void 영수증_구매금액_계산_테스트() {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService();
        Map.Entry<String, Integer> requestVitamin = new AbstractMap.SimpleEntry<>("비타민워터", 6);
        convenienceStoreService.purchaseNoPromotionProducts(requestVitamin);
        Map.Entry<String, Integer> requestCoke = new AbstractMap.SimpleEntry<>("콜라", 5);
        convenienceStoreService.purchasePromotionProduct(requestCoke);

        ReceiptDto receipt = convenienceStoreService.createReceipt(true);
        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(receipt.getFullPrice()).isEqualTo(1500 * 6 + 1000 * 4);
            softly.assertThat(receipt.getPromotionDiscountPrice()).isEqualTo(1000 * 1);
            softly.assertThat(receipt.getMembershipDiscountPrice()).isEqualTo((int) ((1500 * 6 + 1000 * 4) * 0.3));
        });
    }
}