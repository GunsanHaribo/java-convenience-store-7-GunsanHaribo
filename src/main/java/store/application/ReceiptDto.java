package store.application;

import store.domain.PurchasedProduct;

import java.util.ArrayList;
import java.util.List;

public class ReceiptDto {
    private final List<PurchasedProductDto> purchasedProducts;
    private final List<PurchasedProductDto> purchasedPromotionProducts;
    private final int fullPrice;
    private final int promotionDiscountPrice;
    private final int membershipDiscountPrice;

    public ReceiptDto(List<PurchasedProduct> purchasedProducts, List<PurchasedProduct> purchasedPromotionProducts, int fullPrice, int promotionDiscountPrice, int membershipDiscountPrice) {
        this.purchasedProducts = createReceiptDto(purchasedProducts);
        this.purchasedPromotionProducts = createReceiptDto(purchasedPromotionProducts);
        this.fullPrice = fullPrice;
        this.promotionDiscountPrice = promotionDiscountPrice;
        this.membershipDiscountPrice = membershipDiscountPrice;
    }

    private List<PurchasedProductDto> createReceiptDto(List<PurchasedProduct> purchasedProducts) {
        List<PurchasedProductDto> result = new ArrayList<>();
        for (PurchasedProduct product : purchasedProducts) {
            result.add(new PurchasedProductDto(product.getName(), product.getPrice(), product.getQuantity()));
        }
        return result;
    }

    public List<PurchasedProductDto> getPurchasedProducts() {
        return purchasedProducts;
    }

    public List<PurchasedProductDto> getPurchasedPromotionProducts() {
        return purchasedPromotionProducts;
    }

    public int getFullPrice() {
        return fullPrice;
    }

    public int getPromotionDiscountPrice() {
        return promotionDiscountPrice;
    }

    public int getMembershipDiscountPrice() {
        return membershipDiscountPrice;
    }
}
