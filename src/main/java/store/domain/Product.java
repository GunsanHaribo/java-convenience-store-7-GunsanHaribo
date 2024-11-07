package store.domain;

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
        this.quantity = this.quantity.subtractQuantity(requestQuantity);
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
