package store.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

public enum Promotion {
    SORT_DRINK_TWO_PLUS_ONE("탄산2+1", 2, 1, "2024-01-01", "2024-12-31"),
    MANAGER_RECOMMENDATION("MD추천상품", 1, 1, "2024-01-01", "2024-12-31"),
    FLASH_SALE("반짝할인", 1, 1, "2024-11-01", "2024-11-30"),
    NONE("null", 0, 0, "0000-01-01", "0000-01-01");

    private final String promotionName;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    Promotion(String promotionName, int buy, int get, String startDate, String endDate) {
        this.promotionName = promotionName;
        this.buy = buy;
        this.get = get;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
    }

    public boolean isPromotionSalePeriod(LocalDateTime todayDate) {
        return !ChronoLocalDate.from(todayDate).isBefore(this.startDate) && !ChronoLocalDate.from(todayDate).isAfter(this.endDate);
    }

    public static Promotion findPromotionByName(String promotionName) {
        for (Promotion promotion : Promotion.values()) {
            if (promotion.promotionName.equals(promotionName)) {
                return promotion;
            }
        }
        return NONE;
    }
}
