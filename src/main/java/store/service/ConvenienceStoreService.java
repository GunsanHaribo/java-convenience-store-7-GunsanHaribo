package store.service;

import store.application.ProductsDto;
import store.application.PromotionPurchasingResultDto;
import store.application.ReceiptDto;
import store.domain.*;

import java.util.List;
import java.util.Map;

import static store.domain.PromotionPurchaseStatus.*;

public class ConvenienceStoreService {
    private final Promotions promotions = new Promotions("/promotions.md");
    private final Products products = new Products("/products.md", promotions);
    private final Receipt receipt = new Receipt();

    public ProductsDto loadInitialProducts() {
        return new ProductsDto(products.getProducts());
    }

    public PromotionPurchasingResultDto purchasePromotionProduct(Map.Entry<String, Integer> requestProduct) {
        Product promotionProduct = products.extractPromotionProducts(requestProduct.getKey());
        if (promotionProduct != null) {
            int lackOfQuantity = promotionProduct.checkLackOfPromotion(requestProduct.getValue());
            if (lackOfQuantity > 0) {
                int purchasedTotalQuantity = requestProduct.getValue() - lackOfQuantity;
                promotionProduct.subtractQuantity(purchasedTotalQuantity);
                int promotionBenefitQuantity = promotionProduct.calculateNumberOfPromotionProduct(purchasedTotalQuantity);
                receipt.updateFreeProduct(new PurchasedProduct(requestProduct.getKey(), promotionProduct.getPrice(), promotionBenefitQuantity));
                receipt.updateFullPriceProduct(new PurchasedProduct(requestProduct.getKey(), promotionProduct.getPrice(), purchasedTotalQuantity - promotionBenefitQuantity));

                return new PromotionPurchasingResultDto(LACK_OF_PROMOTION_QUANTITY, lackOfQuantity);
            }

            int requiredPromotionAmount = promotionProduct.calculateRequiredPromotionQuantity(requestProduct.getValue());
            if (requiredPromotionAmount > 0) {
                promotionProduct.subtractQuantity(requestProduct.getValue());
                int promotionBenefitQuantity = promotionProduct.calculateNumberOfPromotionProduct(requestProduct.getValue());
                receipt.updateFreeProduct(new PurchasedProduct(requestProduct.getKey(), promotionProduct.getPrice(), promotionBenefitQuantity));
                receipt.updateFullPriceProduct(new PurchasedProduct(requestProduct.getKey(), promotionProduct.getPrice(), requestProduct.getValue() - promotionBenefitQuantity));
                return new PromotionPurchasingResultDto(PROMOTION_CAN_BE_APPLIED, requiredPromotionAmount);
            }
            purchasePurePromotionProduct(requestProduct);
        }
        return new PromotionPurchasingResultDto(PROMOTION_NOT_EXIST, requestProduct.getValue());
    }

    public void purchasePurePromotionProduct(Map.Entry<String, Integer> requestProduct) {
        Product promotionProduct = products.extractPromotionProducts(requestProduct.getKey());
        promotionProduct.subtractQuantity(requestProduct.getValue());

        receipt.updateFreeProduct(new PurchasedProduct(requestProduct.getKey(), promotionProduct.getPrice(), requestProduct.getValue()));
    }

    public void purchaseNoPromotionProducts(Map.Entry<String, Integer> requestProduct) {
        List<Product> noPromotionProducts = products.extractNoPromotionProducts(requestProduct.getKey());
        try {
            noPromotionProducts.getFirst().subtractQuantity(requestProduct.getValue());
            receipt.updateFullPriceProduct(new PurchasedProduct(requestProduct.getKey(), noPromotionProducts.getFirst().getPrice(), requestProduct.getValue()));
        } catch (IllegalArgumentException e) {
            if (noPromotionProducts.size() > 1) {
                noPromotionProducts.getLast().subtractQuantity(requestProduct.getValue());
                receipt.updateFullPriceProduct(new PurchasedProduct(requestProduct.getKey(), noPromotionProducts.getFirst().getPrice(), requestProduct.getValue()));
            }
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public ReceiptDto createReceipt(boolean isMembershipDiscountActive) {
        int fullPrice = receipt.calculateFullPrice();
        int discountPrice = receipt.calculatePromotionDiscount();

        int membershipDiscountPrice = 0;
        if (isMembershipDiscountActive) {
            MembershipDiscount membershipDiscount = new MembershipDiscount();
            membershipDiscountPrice = membershipDiscount.discountTotalPrice(fullPrice);
        }
        return new ReceiptDto(receipt.getFullPriceProducts(), receipt.getFreeProducts(), fullPrice, discountPrice, membershipDiscountPrice);
    }
}
