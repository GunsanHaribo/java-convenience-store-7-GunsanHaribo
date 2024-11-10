package store.domain;

public class Quantity {
    private final int quantity;

    public Quantity(int quantity) {
        this.quantity = quantity;
    }

    public Quantity subtractQuantity(int requestQuantity) {
        if (isPurchasable(requestQuantity)) {
            return new Quantity(this.quantity - requestQuantity);
        }
        throw new IllegalArgumentException("[ERROR] 재고보다 큰 숫자가 들어왔습니다");
    }

    public int calculateLackOfQuantity(int requestQuantity) {
        return requestQuantity - this.quantity;
    }

    public boolean isPurchasable(int requestQuantity) {
        return quantity >= requestQuantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
