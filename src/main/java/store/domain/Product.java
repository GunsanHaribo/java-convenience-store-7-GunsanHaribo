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

    public void subtractQuantity(int requestQuantity) {
        this.quantity = quantity.subtractQuantity(requestQuantity + promotionQuantity(requestQuantity));
    }

    private int promotionQuantity(int requestQuantity) {
        int promotionQuantity = 0;
        if (promotion != Promotion.NONE && promotion.isPromotionSalePeriod(DateTimes.now())) {
            promotionQuantity = (requestQuantity / promotion.getBuy()) * this.promotion.getGet();
        }
        return promotionQuantity;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
