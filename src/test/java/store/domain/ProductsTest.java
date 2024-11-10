package store.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductsTest {
    private final Promotions promotions = new Promotions("/promotions.md");
    private final Products products = new Products("/products.md", promotions);

    @DisplayName("프로모션_상품_추출_테스트")
    @ParameterizedTest
    @CsvSource(value = {"콜라:탄산2+1", "오렌지주스:MD추천상품", "감자칩:반짝할인"}, delimiter = ':')
    void 프로모션_상품_추출_테스트(String productName, String expectedPromotionName){
        Product product = products.extractPromotionProducts(productName);
        String promotionName = product.getPromotion()
                .map(Promotion::getPromotionName)
                .orElse("");

        assertThat(promotionName).isEqualTo(expectedPromotionName);
    }

    @DisplayName("프로모션_상품_추출_예외_테스트")
    @Test
    void 프로모션_상품_추출_예외_테스트(){
        Product product = products.extractPromotionProducts("물");

        assertThat(product).isNull();
    }

    @DisplayName("상품 초기화 테스트")
    @Test
    void 상품_초기화_테스트() {

        List<Product> actualProducts = products.getProducts();
        Product product = actualProducts.get(0);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(product.getName()).isEqualTo("콜라");
            softly.assertThat(product.getQuantity().getQuantity()).isEqualTo(10);
            softly.assertThat(product.getPromotion().get()).isEqualTo(promotions.findPromotionByName("탄산2+1"));
        });
    }
}