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
                return calculateRequiredPromotionQuantity(promotionProduct, purchasedTotalQuantity, requestProduct, LACK_OF_PROMOTION_QUANTITY, lackOfQuantity);
            }

            int requiredPromotionAmount = promotionProduct.calculateRequiredPromotionQuantity(requestProduct.getValue());
            if (requiredPromotionAmount > 0) {
                return calculateRequiredPromotionQuantity(promotionProduct, requestProduct.getValue(), requestProduct, PROMOTION_CAN_BE_APPLIED, requiredPromotionAmount);
            }
            purchasePurePromotionProduct(requestProduct);
        }
        return new PromotionPurchasingResultDto(PROMOTION_NOT_EXIST, requestProduct.getValue());
    }

    private PromotionPurchasingResultDto calculateRequiredPromotionQuantity(Product promotionProduct, Integer requestProduct, Map.Entry<String, Integer> requestProduct1, PromotionPurchaseStatus promotionCanBeApplied, int requiredPromotionAmount) {
        promotionProduct.subtractQuantity(requestProduct);
        int promotionBenefitQuantity = promotionProduct.calculateNumberOfPromotionProduct(requestProduct);
        receipt.updateFreeProduct(new PurchasedProduct(requestProduct1.getKey(), promotionProduct.getPrice(), promotionBenefitQuantity));
        receipt.updateFullPriceProduct(new PurchasedProduct(requestProduct1.getKey(), promotionProduct.getPrice(), requestProduct - promotionBenefitQuantity));
        return new PromotionPurchasingResultDto(promotionCanBeApplied, requiredPromotionAmount);
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
