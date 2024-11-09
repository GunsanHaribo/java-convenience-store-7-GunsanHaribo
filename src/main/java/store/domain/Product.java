package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;

public class Product {
    private final String name;
    private final int price;
    private Quantity quantity;
    private final Promotion promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = new Quantity(quantity);
        this.promotion = Promotion.findPromotionByName(promotion);
    }

    public void subtractQuantityWithoutPromotion(int requestQuantity) {
        this.quantity = quantity.subtractQuantityWithoutPromotion(requestQuantity);
    }

    public int calculatePromotionQuantity(int requestQuantity) {
        int promotionQuantity = this.promotion.calculateRequiredPromotionQuantity(requestQuantity);
        int totalRequestQuantity = requestQuantity + promotionQuantity;
        if (totalRequestQuantity <= this.quantity.getQuantity()) {
            return promotionQuantity;
        }
        return 0;
    }

    // TODO: 11/9/24 10줄로 리펙토링 필요
    public int subtractQuantityWithPromotion(int requestQuantity) {
        int lackOfQuantity = 0;
        int totalRequestQuantity = requestQuantity + promotionQuantity(requestQuantity);
        try {
            this.quantity = quantity.subtractQuantityWithoutPromotion(totalRequestQuantity);
        } catch (IllegalArgumentException e) {
            lackOfQuantity = this.quantity.calculateLackOfQuantity(totalRequestQuantity);
            this.quantity = new Quantity(0);
        }
        return lackOfQuantity;
    }

    private int promotionQuantity(int requestQuantity) {
        int promotionQuantity = 0;
        if (isPromotionProduct()) {
            promotionQuantity = (requestQuantity / promotion.getBuy()) * this.promotion.getGet();
        }
        return promotionQuantity;
    }

    public boolean isPromotionProduct() {
        return promotion != Promotion.NONE && promotion.isPromotionSalePeriod(DateTimes.now());
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String getName() {
        return name;
    }
}
