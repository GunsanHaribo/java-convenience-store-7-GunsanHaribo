package store.service;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.application.ProductDto;
import store.application.ProductsDto;

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
}