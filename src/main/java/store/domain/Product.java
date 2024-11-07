package store.domain;

public class Product {
    private Quantity quantity;

    public Product(int quantity) {
        this.quantity = new Quantity(quantity);
    }

    public void subtractQuantity(int requestQuantity) {
        this.quantity = this.quantity.subtractQuantity(requestQuantity);
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
