package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;

import java.util.Optional;

public class Product {
    private final String name;
    private final int price;
    private Quantity quantity;
    private final Promotion promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = new Quantity(quantity);
        this.promotion = promotion;
    }

    public void subtractQuantity(int requestQuantity) {
        this.quantity = quantity.subtractQuantity(requestQuantity);
    }

    public int calculateRequiredPromotionQuantity(int requestQuantity) {
        int requiredPromotionQuantity = this.promotion.calculateRequiredPromotionQuantity(requestQuantity);
        if (requiredPromotionQuantity > 0) {
            int totalRequestQuantity = requestQuantity + requiredPromotionQuantity;
            if (totalRequestQuantity <= this.quantity.getQuantity()) {
                return requiredPromotionQuantity;
            }
        }
        return 0;
    }

    public int checkLackOfPromotion(int requestQuantity) {
        if (!this.quantity.isPurchasable(requestQuantity)) {
            return this.quantity.calculateLackOfQuantity(requestQuantity);
        }
        return 0;
    }

    public boolean isPromotionProduct() {
        return promotion != null && promotion.isPromotionSalePeriod(DateTimes.now());
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Optional<Promotion> getPromotion() {
        return Optional.ofNullable(promotion);
    }
}
