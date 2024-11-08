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
        this.quantity = quantity.subtractQuantity(requestQuantity);

        if (promotion != Promotion.NONE && promotion.isPromotionSalePeriod(DateTimes.now())) {
            int promotionCoefficient = requestQuantity / promotion.getBuy();
            this.quantity = quantity.subtractQuantity(promotionCoefficient * this.promotion.getGet());
        }
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
