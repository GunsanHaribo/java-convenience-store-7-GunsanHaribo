package store.controller;

import store.application.PromotionPurchasingResultDto;
import store.application.ReceiptDto;
import store.service.ConvenienceStoreService;

import java.util.AbstractMap;
import java.util.Map;

import static store.domain.PromotionPurchaseStatus.*;
import static store.view.InputView.*;
import static store.view.OutputView.printProducts;
import static store.view.OutputView.printReceipt;

public class ConvenienceStoreController {
    private final ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService();

    public void run() {
        while (true) {
            printProducts(convenienceStoreService.loadInitialProducts());
            Map<String, Integer> products = readProducts();

            purchaseProducts(products);
            ReceiptDto receiptDto = createReceipt();
            printReceipt(receiptDto);
            if (!readConfirmOtherProduct()) {
                break;
            }
        }
    }

    private void purchaseProducts(Map<String, Integer> products) {
        for (Map.Entry<String, Integer> requestProduct : products.entrySet()) {
            purchaseProducts(requestProduct);
        }
    }

    private ReceiptDto createReceipt() {
        Boolean userAnswer = readConfirmMemberShipDiscount();
        ReceiptDto receiptDto = convenienceStoreService.createReceipt(userAnswer);
        return receiptDto;
    }

    private void purchaseProducts(Map.Entry<String, Integer> requestProduct) {
        PromotionPurchasingResultDto promotionPurchasingResultDto = convenienceStoreService.purchasePromotionProduct(requestProduct);
        purchasePromotionProduct(promotionPurchasingResultDto.getStatus() == PROMOTION_NOT_EXIST, requestProduct, promotionPurchasingResultDto);
        checkLackOfQuantity(requestProduct, promotionPurchasingResultDto);
        checkPromotionApplicability(requestProduct, promotionPurchasingResultDto);
    }

    private void checkPromotionApplicability(Map.Entry<String, Integer> requestProduct, PromotionPurchasingResultDto promotionPurchasingResultDto) {
        if (promotionPurchasingResultDto.getStatus() == PROMOTION_CAN_BE_APPLIED) {
            Boolean userAnswer = readConfirmPromotionAddition(requestProduct.getKey(), promotionPurchasingResultDto.getQuantity());
            if (userAnswer) {
                Map.Entry<String, Integer> otherProduct = new AbstractMap.SimpleEntry<>(requestProduct.getKey(), promotionPurchasingResultDto.getQuantity());
                convenienceStoreService.purchasePromotionProduct(otherProduct);
            }
        }
    }

    private void checkLackOfQuantity(Map.Entry<String, Integer> requestProduct, PromotionPurchasingResultDto promotionPurchasingResultDto) {
        if (promotionPurchasingResultDto.getStatus() == LACK_OF_PROMOTION_QUANTITY) {
            Boolean userAnswer = readConfirmNonDiscountedPurchase(requestProduct.getKey(), requestProduct.getValue());
            purchasePromotionProduct(userAnswer, requestProduct, promotionPurchasingResultDto);
        }
    }

    private void purchasePromotionProduct(boolean promotionPurchasingResultDto, Map.Entry<String, Integer> requestProduct, PromotionPurchasingResultDto promotionPurchasingResultDto1) {
        if (promotionPurchasingResultDto) {
            Map.Entry<String, Integer> otherProduct = new AbstractMap.SimpleEntry<>(requestProduct.getKey(), promotionPurchasingResultDto1.getQuantity());
            convenienceStoreService.purchaseNoPromotionProducts(otherProduct);
        }
    }
}
