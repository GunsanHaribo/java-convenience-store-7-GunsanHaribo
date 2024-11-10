package store.service;

import store.application.ProductsDto;
import store.application.PromotionPurchasingResultDto;
import store.domain.Product;
import store.domain.Products;
import store.domain.Promotions;

import java.util.List;
import java.util.Map;

import static store.domain.PromotionPurchaseStatus.*;

public class ConvenienceStoreService {
    private final Promotions promotions = new Promotions("/promotions.md");
    private final Products products = new Products("/products.md", promotions);

    public ProductsDto loadInitialProducts() {
        return new ProductsDto(products.getProducts());
    }

    public PromotionPurchasingResultDto purchasePromotionProduct(Map.Entry<String, Integer> requestProduct) {
        Product promotionProduct = products.extractPromotionProducts(requestProduct.getKey());
        if (promotionProduct != null) {
            int lackOfQuantity = products.checkLackOfPromotion(requestProduct.getValue(), promotionProduct);
            if (lackOfQuantity > 0) {
                return new PromotionPurchasingResultDto(LACK_OF_PROMOTION_QUANTITY, lackOfQuantity);
            }

            int requiredPromotionAmount = products.checkPromotionApplied(requestProduct.getValue(), promotionProduct);
            if (requiredPromotionAmount > 0) {
                return new PromotionPurchasingResultDto(PROMOTION_CAN_BE_APPLIED, requiredPromotionAmount);
            }
        }
        return new PromotionPurchasingResultDto(PROMOTION_NOT_EXIST, requestProduct.getValue());
    }

    public void purchaseNoPromotionProducts(Map.Entry<String, Integer> requestProduct) {
        List<Product> noPromotionProducts = products.extractNoPromotionProducts(requestProduct.getKey());
        try {
            noPromotionProducts.getFirst().subtractQuantity(requestProduct.getValue());
        } catch (IllegalArgumentException e) {
            if (noPromotionProducts.size() > 1) {
                noPromotionProducts.getLast().subtractQuantity(requestProduct.getValue());
            }
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
