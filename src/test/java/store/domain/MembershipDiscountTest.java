package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipDiscountTest {
    @DisplayName("멤버쉽 할인 테스트")
    @ParameterizedTest
    @MethodSource("provideTotalPriceAndDiscountedTotalPrice")
    void 멤버쉽_할인_테스트(int totalPrice, double expectedDiscountedTotalPrice) {
        MembershipDiscount membershipDiscount = new MembershipDiscount();
        int discountedTotalPrice = membershipDiscount.discountTotalPrice(totalPrice);

        assertThat(discountedTotalPrice).isEqualTo((int) expectedDiscountedTotalPrice);
    }

    static Stream<Arguments> provideTotalPriceAndDiscountedTotalPrice() {
        return Stream.of(
                Arguments.of(8000, 8000 * 0.30),
                Arguments.of(26667, 8000)
        );
    }
}