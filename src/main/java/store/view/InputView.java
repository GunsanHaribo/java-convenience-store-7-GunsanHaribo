package store.view;

import camp.nextstep.edu.missionutils.Console;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputView {
    private InputView() {
    }

    public static Map<String, Integer> readProducts() {
        Map<String, Integer> products = new HashMap<>();
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        List<String> items = Arrays.stream(Console.readLine().split(",")).toList();

        for (String item : items) {
            item = item.replace("[", "").replace("]", "");
            String name = item.substring(0, item.indexOf("-"));
            int quantity = Integer.parseInt(item.substring(item.indexOf("-") + 1));

            products.put(name, quantity);
        }
        return products;
    }

    public static Boolean readConfirmNonDiscountedPurchase(String productName, int lackOfQuantity) {
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n", productName, lackOfQuantity);
        String answer = Console.readLine();
        if (answer.equals("Y")) {
            return true;
        }
        return false;
    }

    public static Boolean readConfirmPromotionAddition(String productName, int freeQuantity) {
        System.out.printf("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n", productName, freeQuantity);
        String answer = Console.readLine();
        if (answer.equals("Y")) {
            return true;
        }
        return false;
    }

    public static Boolean readConfirmMemberShipDiscount() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        String answer = Console.readLine();
        if (answer.equals("Y")) {
            return true;
        }
        return false;
    }

    public static Boolean readConfirmOtherProduct(){
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        String answer = Console.readLine();
        if (answer.equals("Y")) {
            return true;
        }
        return false;
    }
}
