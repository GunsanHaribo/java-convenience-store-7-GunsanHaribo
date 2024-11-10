package store.view;

import store.application.ProductDto;
import store.application.ProductsDto;
import store.application.PurchasedProductDto;
import store.application.ReceiptDto;

import java.text.DecimalFormat;

public class OutputView {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    private OutputView() {
    }

    public static void printProducts(ProductsDto productsDto) {
        System.out.println("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다.");

        for (ProductDto productDto : productsDto.getProducts()) {
            String quantityInfo = "";
            if (productDto.getQuantity() > 0) {
                quantityInfo = productDto.getQuantity() + "개";
            }
            if (productDto.getQuantity() <= 0) {
                quantityInfo = "재고 없음";
            }
            System.out.printf("- %s %s원 %s %s%n", productDto.getName(), decimalFormat.format(productDto.getPrice()), quantityInfo, productDto.getPromotion());
        }
    }

    public static void printReceipt(ReceiptDto receiptDto) {
        System.out.println("============== W 편의점 ==================");
        System.out.println("상품명\t\t수량\t금액");

        int totalAmount = 0;
        for (PurchasedProductDto purchasedProductDto : receiptDto.getPurchasedProducts()) {
            totalAmount += purchasedProductDto.getQuantity();
            System.out.printf("%s\t\t%d\t%s\n", purchasedProductDto.getName(), purchasedProductDto.getQuantity(), decimalFormat.format(purchasedProductDto.getQuantity() * purchasedProductDto.getPrice()));
        }

        System.out.println("============= 증    정 ===============");
        for (PurchasedProductDto purchasedProductDto : receiptDto.getPurchasedPromotionProducts()) {
            System.out.printf("%s\t\t%d\n", purchasedProductDto.getName(), purchasedProductDto.getQuantity());
        }

        System.out.println("====================================");
        System.out.printf("총구매액\t\t%s\t%s\n", totalAmount, decimalFormat.format(receiptDto.getFullPrice() + receiptDto.getPromotionDiscountPrice()));
        System.out.printf("행사할인\t\t\t-%s\n", decimalFormat.format(receiptDto.getPromotionDiscountPrice()));
        System.out.printf("멤버십할인\t\t\t-%s\n", decimalFormat.format(receiptDto.getMembershipDiscountPrice()));
        System.out.printf("내실돈\t\t\t%s\n", decimalFormat.format(receiptDto.getFullPrice() - receiptDto.getMembershipDiscountPrice()));
    }
}
