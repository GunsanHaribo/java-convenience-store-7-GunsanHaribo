package store.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

public class Promotion {
    private final String promotionName;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String promotionName, int buy, int get, String startDate, String endDate) {
        this.promotionName = promotionName;
        this.buy = buy;
        this.get = get;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
    }

    public boolean isPromotionSalePeriod(LocalDateTime todayDate) {
        return !ChronoLocalDate.from(todayDate).isBefore(this.startDate) && !ChronoLocalDate.from(todayDate).isAfter(this.endDate);
    }

    public int calculateRequiredPromotionQuantity(int requestQuantity) {
        int promotionQuantity = requestQuantity % (this.buy + this.get);
        if (promotionQuantity == this.buy) {
            return this.get;
        }
        return 0;
    }

    public boolean isSameName(String promotionName) {
        return this.promotionName.equals(promotionName);
    }

    public String getPromotionName() {
        return promotionName;
    }
}
