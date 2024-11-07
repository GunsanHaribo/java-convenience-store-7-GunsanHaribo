package store.domain;

public class MembershipDiscount {
    private static final double DISCOUNT_RATE = 0.30;
    private static final int DISCOUNT_LIMIT = 8000;

    public int discountTotalPrice(int totalPrice) {
        int discountedPrice = (int) (totalPrice * DISCOUNT_RATE);
        return Math.min(discountedPrice, DISCOUNT_LIMIT);
    }
}
