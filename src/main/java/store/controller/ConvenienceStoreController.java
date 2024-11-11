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
            Map<String, Integer> items = readItems();
            for (Map.Entry<String, Integer> requestProduct : items.entrySet()) {
                PromotionPurchasingResultDto promotionPurchasingResultDto = convenienceStoreService.purchasePromotionProduct(requestProduct);
                if (promotionPurchasingResultDto.getStatus() == PROMOTION_NOT_EXIST) {
                    Map.Entry<String, Integer> otherProduct = new AbstractMap.SimpleEntry<>(requestProduct.getKey(), promotionPurchasingResultDto.getQuantity());
                    convenienceStoreService.purchaseNoPromotionProducts(otherProduct);
                }
                if (promotionPurchasingResultDto.getStatus() == LACK_OF_PROMOTION_QUANTITY) {
                    Boolean userAnswer = readConfirmNonDiscountedPurchase(requestProduct.getKey(), requestProduct.getValue());
                    if (userAnswer) {
                        Map.Entry<String, Integer> otherProduct = new AbstractMap.SimpleEntry<>(requestProduct.getKey(), promotionPurchasingResultDto.getQuantity());
                        convenienceStoreService.purchaseNoPromotionProducts(otherProduct);
                    }
                }
                if (promotionPurchasingResultDto.getStatus() == PROMOTION_CAN_BE_APPLIED) {
                    Boolean userAnswer = readConfirmPromotionAddition(requestProduct.getKey(), promotionPurchasingResultDto.getQuantity());
                    if (userAnswer) {
                        Map.Entry<String, Integer> otherProduct = new AbstractMap.SimpleEntry<>(requestProduct.getKey(), promotionPurchasingResultDto.getQuantity());
                        convenienceStoreService.purchasePromotionProduct(otherProduct);
                    }
                }
            }
            Boolean userAnswer = readConfirmMemberShipDiscount();
            ReceiptDto receiptDto = convenienceStoreService.createReceipt(userAnswer);
            printReceipt(receiptDto);

            if (!readConfirmOtherProduct()) {
                break;
            }
        }
    }
}
