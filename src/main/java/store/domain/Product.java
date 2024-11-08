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

    public int subtractQuantity(int requestQuantity) {
        int lackOfQuantity = 0;
        int totalRequestQuantity = requestQuantity + promotionQuantity(requestQuantity);
        try {
            this.quantity = quantity.subtractQuantity(totalRequestQuantity);
        } catch (IllegalArgumentException e) {
            if (isPromotion()) {
                lackOfQuantity = quantity.calculateLackOfQuantity(totalRequestQuantity);
                this.quantity = quantity.subtractQuantity(requestQuantity + lackOfQuantity);
                return lackOfQuantity;
            }
            throw new IllegalArgumentException(e.getMessage());
        }
        return lackOfQuantity;
    }

    private int promotionQuantity(int requestQuantity) {
        int promotionQuantity = 0;
        if (isPromotion()) {
            promotionQuantity = (requestQuantity / promotion.getBuy()) * this.promotion.getGet();
        }
        return promotionQuantity;
    }

    private boolean isPromotion() {
        return promotion != Promotion.NONE && promotion.isPromotionSalePeriod(DateTimes.now());
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
