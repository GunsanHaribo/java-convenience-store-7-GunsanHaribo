package store.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public Products(String filePath, Promotions promotions) {
        this.products = createProducts(filePath, promotions);
    }

    // TODO: 11/10/24 10줄
    private List<Product> createProducts(String filePath, Promotions promotions) {
        List<Product> products = new ArrayList<>();
        try (InputStream inputStream = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.stream(line.split(",")).toList();
                String name = values.get(0);
                int price = Integer.parseInt(values.get(1).trim());
                int quantity = Integer.parseInt(values.get(2).trim());
                Promotion promotion = promotions.findPromotionByName(values.get(3).trim());
                products.add(new Product(name, price, quantity, promotion));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void purchaseProducts(Map<String, Integer> requestProducts) {
        for (Map.Entry<String, Integer> requestProduct : requestProducts.entrySet()) {
            purchaseProduct(requestProduct);
        }
    }

    // TODO: 11/9/24 10줄로 리펙터링 필요
    private void purchaseProduct(Map.Entry<String, Integer> requestProduct) {
        List<Product> promotionProducts = extractPromotionProducts(requestProduct.getKey());
        List<Product> noPromotionProducts = extractNoPromotionProducts(requestProduct.getKey());
        boolean hasPromotionProducts = !promotionProducts.isEmpty();
        boolean hasNoPromotionProducts = !noPromotionProducts.isEmpty();
        boolean hasOnlyNoPromotionProducts = promotionProducts.isEmpty() && hasNoPromotionProducts;

        if (hasPromotionProducts) {
            purchasePromotionProduct(requestProduct.getValue(), promotionProducts, noPromotionProducts);
        }
        if (hasOnlyNoPromotionProducts) {
            purchaseNoPromotionProduct(requestProduct.getValue(), noPromotionProducts);
        }
    }

    private void purchaseNoPromotionProduct(int requestProductValue, List<Product> noPromotionProducts) {
        for (Product noPromotionProduct : noPromotionProducts) {
            noPromotionProduct.subtractQuantityWithoutPromotion(requestProductValue);
        }
    }

    private void purchasePromotionProduct(int requestProductValue, List<Product> promotionProducts, List<Product> noPromotionProducts) {
        int lackOfQuantity = promotionProducts.getFirst().subtractQuantityWithPromotion(requestProductValue, true);
        if (lackOfQuantity > 0) {
            purchaseNoPromotionProducts(noPromotionProducts, lackOfQuantity);
        }
    }

    private void purchaseNoPromotionProducts(List<Product> noPromotionProducts, int lackOfQuantity) {
        try {
            noPromotionProducts.getFirst().subtractQuantityWithoutPromotion(lackOfQuantity);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("[ERROR] 프로모션 이외 일반 상품 재고가 없습니다.");
        }
    }

    private List<Product> extractPromotionProducts(String requestProductName) {
        List<Product> promotionProducts = new ArrayList<>();
        for (Product product : products) {
            if (isPromotionProduct(requestProductName, product)) {
                promotionProducts.add(product);
            }
        }
        return promotionProducts;
    }

    private List<Product> extractNoPromotionProducts(String requestProductName) {
        List<Product> nonePromotionProducts = new ArrayList<>();
        for (Product product : products) {
            if (isNonePromotionProduct(requestProductName, product)) {
                nonePromotionProducts.add(product);
            }
        }
        return nonePromotionProducts;
    }

    private boolean isNonePromotionProduct(String requestProductName, Product product) {
        return product.isSameName(requestProductName) && !product.isPromotionProduct();
    }

    private boolean isPromotionProduct(String requestProductName, Product product) {
        return product.isSameName(requestProductName) && product.isPromotionProduct();
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
}