package store.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receipt {
    private final List<PurchasedProduct> fullPriceProducts = new ArrayList<>();
    private final List<PurchasedProduct> freeProducts = new ArrayList<>();

    public void updateFullPriceProduct(PurchasedProduct purchasedProduct) {
        fullPriceProducts.add(purchasedProduct);
    }

    public void updateFreeProduct(PurchasedProduct purchasedProduct) {
        freeProducts.add(purchasedProduct);
    }

    public int calculateFullPrice() {
        return fullPriceProducts.stream()
                .mapToInt(purchasedProduct -> purchasedProduct.getPrice() * purchasedProduct.getQuantity())
                .sum();
    }

    public int calculatePromotionDiscount() {
        return freeProducts.stream()
                .mapToInt(purchasedProduct -> purchasedProduct.getPrice() * purchasedProduct.getQuantity())
                .sum();
    }

    public List<PurchasedProduct> getFullPriceProducts() {
        return Collections.unmodifiableList(fullPriceProducts);
    }

    public List<PurchasedProduct> getFreeProducts() {
        return Collections.unmodifiableList(freeProducts);
    }
}

