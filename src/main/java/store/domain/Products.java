package store.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public void purchaseProducts(Map<String, Integer> requestProducts) {
        for (Map.Entry<String, Integer> requestProduct : requestProducts.entrySet()) {
            purchaseProduct(requestProduct);
        }
    }

    private void purchaseProduct(Map.Entry<String, Integer> requestProduct) {
        Product promotionProduct = null;
        Product nonePromotionProduct = null;
        for (Product product : products) {
            if (isPromotionProduct(requestProduct, product)) {
                promotionProduct = product;
            }
            if (isNonePromotionProduct(requestProduct, product)) {
                nonePromotionProduct = product;
            }
        }

        int lackOfQuantity = 0;
        if (promotionProduct != null) {
            lackOfQuantity = promotionProduct.subtractQuantity(requestProduct.getValue());

            if (nonePromotionProduct == null && lackOfQuantity > 0) {
                throw new IllegalArgumentException("[ERROR] 프로모션 이후 재고가 없습니다.");
            }
        }

        if (nonePromotionProduct != null) {
            if (promotionProduct == null) {
                nonePromotionProduct.subtractQuantity(requestProduct.getValue());
            }
            if (lackOfQuantity > 0) {
                nonePromotionProduct.subtractQuantity(lackOfQuantity);
            }
        }
    }

    private boolean isNonePromotionProduct(Map.Entry<String, Integer> requestProduct, Product product) {
        return product.getName().equals(requestProduct.getKey()) && product.getPromotion() == Promotion.NONE;
    }

    private boolean isPromotionProduct(Map.Entry<String, Integer> requestProduct, Product product) {
        return product.getName().equals(requestProduct.getKey()) && product.getPromotion() != Promotion.NONE;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
}