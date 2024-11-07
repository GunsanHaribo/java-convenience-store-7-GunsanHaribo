package store.domain;

public class Quantity {
    private final int quantity;

    public Quantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isPurchasable(int requestQuantity) {
        return quantity >= requestQuantity;
    }
}
