package store.view;

import store.application.ProductDto;
import store.application.ProductsDto;
import store.application.PurchasedProductDto;
import store.application.ReceiptDto;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class OutputView {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    private OutputView() {
    }

    public static void printProducts(ProductsDto productsDto) {
        System.out.println("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다.");

        for (Map.Entry<String, List<ProductDto>> productDtos : productsDto.getProducts().entrySet()) {
            printExistingProducts(productDtos);
        }
    }

    private static void printExistingProducts(Map.Entry<String, List<ProductDto>> productDtos) {
        for (ProductDto productDto : productDtos.getValue()) {
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
        printReceiptHeader();
        int totalAmount = 0;
        totalAmount = printEachProducts(receiptDto, totalAmount);

        printPromotion(receiptDto);
        printFinalReceipt(receiptDto, totalAmount);
    }

    private static void printReceiptHeader() {
        System.out.println("============== W 편의점 ==================");
        System.out.println("상품명\t\t수량\t금액");
    }

    private static int printEachProducts(ReceiptDto receiptDto, int totalAmount) {
        for (PurchasedProductDto purchasedProductDto : receiptDto.getPurchasedProducts()) {
            totalAmount += purchasedProductDto.getQuantity();
            System.out.printf("%s\t\t%d\t%s\n", purchasedProductDto.getName(), purchasedProductDto.getQuantity(), decimalFormat.format(purchasedProductDto.getQuantity() * purchasedProductDto.getPrice()));
        }
        return totalAmount;
    }

    private static void printFinalReceipt(ReceiptDto receiptDto, int totalAmount) {
        System.out.println("====================================");
        System.out.printf("총구매액\t\t%s\t%s\n", totalAmount, decimalFormat.format(receiptDto.getFullPrice() + receiptDto.getPromotionDiscountPrice()));
        System.out.printf("행사할인\t\t\t-%s\n", decimalFormat.format(receiptDto.getPromotionDiscountPrice()));
        System.out.printf("멤버십할인\t\t\t-%s\n", decimalFormat.format(receiptDto.getMembershipDiscountPrice()));
        System.out.printf("내실돈\t\t\t%s\n", decimalFormat.format(receiptDto.getFullPrice() - receiptDto.getMembershipDiscountPrice()));
    }

    private static void printPromotion(ReceiptDto receiptDto) {
        System.out.println("============= 증    정 ===============");
        for (PurchasedProductDto purchasedProductDto : receiptDto.getPurchasedPromotionProducts()) {
            System.out.printf("%s\t\t%d\n", purchasedProductDto.getName(), purchasedProductDto.getQuantity());
        }
    }
}
