package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionTest {
    @DisplayName("오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용")
    @ParameterizedTest
    @MethodSource("providePromotionsAndTodayDate")
    void 오늘_날짜가_프로모션_기간_내에_포함된_경우에만_할인을_적용_테스트(Promotion promotion, LocalDateTime todayDate, boolean isPromotionSalePeriod) {
        assertThat(promotion.isPromotionSalePeriod(todayDate)).isEqualTo(isPromotionSalePeriod);
    }

    static Stream<Arguments> providePromotionsAndTodayDate() {
        return Stream.of(
                Arguments.of(Promotion.MANAGER_RECOMMENDATION, LocalDateTime.parse("2024-01-01T00:00:00"), true),
                Arguments.of(Promotion.MANAGER_RECOMMENDATION, LocalDateTime.parse("2025-01-01T00:00:00"), false),
                Arguments.of(Promotion.FLASH_SALE, LocalDateTime.parse("2024-11-01T00:00:00"), true),
                Arguments.of(Promotion.FLASH_SALE, LocalDateTime.parse("2024-12-01T00:00:00"), false),
                Arguments.of(Promotion.NONE, LocalDateTime.parse("2024-12-01T00:00:00"), false)
        );
    }
}