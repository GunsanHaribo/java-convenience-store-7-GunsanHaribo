package store.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductsTest {
    private final Promotions promotions = new Promotions("/promotions.md");

    @DisplayName("프로모션 기간 중이라면 프로모션 재고를 우선 차감, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.")
    @Test
    void 프로모션_기간_중이라면_프로모션_재고를_우선_차감_프로모션_재고가_부족할_경우에는_일반_재고를_사용() {
        Products products = new Products(
                List.of(new Product("콜라", 1000, 10, null),
                        new Product("콜라", 1000, 10, promotions.findPromotionByName("탄산2+1"))));
        Map<String, Integer> requestProducts = new HashMap<>() {
            {
                put("콜라", 13);
            }
        };
        products.purchaseProducts(requestProducts);
        List<Product> actualProducts = products.getProducts();
        int nonePromotionProductQuantity = actualProducts.get(0).getQuantity().getQuantity();
        int promotionProductQuantity = actualProducts.get(1).getQuantity().getQuantity();

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(nonePromotionProductQuantity).isEqualTo(7);
            softly.assertThat(promotionProductQuantity).isEqualTo(0);
        });
    }

    @DisplayName("프로모션_재고가_없을때_테스트 입니다.")
    @Test
    void 프로모션_재고가_없을때_테스트() {
        Products products = new Products(
                List.of(new Product("콜라", 1000, 10, null)));
        Map<String, Integer> requestProducts = new HashMap<>() {
            {
                put("콜라", 8);
            }
        };
        products.purchaseProducts(requestProducts);
        List<Product> actualProducts = products.getProducts();
        int nonePromotionProductQuantity = actualProducts.get(0).getQuantity().getQuantity();

        assertThat(nonePromotionProductQuantity).isEqualTo(2);
    }

    @DisplayName("프로모션 기간중에 프로모션 재고 밖에 없을때 초과시 예외 테스트 입니다.")
    @Test
    void 프로모션_기간중에_프로모션_재고_밖에_없을때_초과시_예외테스트() {
        Products products = new Products(
                List.of(new Product("콜라", 1000, 10, promotions.findPromotionByName("탄산2+1"))));
        Map<String, Integer> requestProducts = new HashMap<>() {
            {
                put("콜라", 11);
            }
        };

        assertThatThrownBy(() -> {
            products.purchaseProducts(requestProducts);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 초기화 테스트")
    @Test
    void 상품_초기화_테스트() {

        Products products = new Products("/products.md", promotions);
        List<Product> actualProducts = products.getProducts();
        Product product = actualProducts.get(0);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(product.getName()).isEqualTo("콜라");
            softly.assertThat(product.getQuantity().getQuantity()).isEqualTo(10);
            softly.assertThat(product.getPromotion()).isEqualTo(promotions.findPromotionByName("탄산2+1"));
        });
    }
}