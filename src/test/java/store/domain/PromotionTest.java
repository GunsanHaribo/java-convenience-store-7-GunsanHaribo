package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static store.domain.Promotion.findPromotionByName;

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

    @DisplayName("프로모션 혜택을 받을 수 있는 상품의 갯수를 계산")
    @ParameterizedTest
    @CsvSource(value = {"탄산2+1:2:1", "탄산2+1:3:0", "MD추천상품:3:1", "MD추천상품:2:0", "null:0:0"}, delimiter = ':')
    void 프로모션_혜택을_받을_수_있는_상품의_갯수를_계산(String promotionName, int requestQuantity, int promotionQuantity) {
        Promotion promotion = findPromotionByName(promotionName);
        int requiredPromotionQuantity = promotion.calculateRequiredPromotionQuantity(requestQuantity);

        assertThat(requiredPromotionQuantity).isEqualTo(promotionQuantity);
    }
}