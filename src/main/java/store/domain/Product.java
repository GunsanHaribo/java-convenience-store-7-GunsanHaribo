package store.domain;

public class Product {
    private Quantity quantity;
    private final Promotion promotion;

    public Product(int quantity, String promotion) {
        this.quantity = new Quantity(quantity);
        this.promotion = Promotion.findPromotionByName(promotion);
    }

    public void subtractQuantity(int requestQuantity) {
        this.quantity = this.quantity.subtractQuantity(requestQuantity);
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
