package store.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Products {
    private final List<Product> products;

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

    public Product extractPromotionProducts(String requestProductName) {
        Product promotionProduct = null;
        for (Product product : products) {
            if (isPromotionProduct(requestProductName, product)) {
                promotionProduct = product;
            }
        }
        return promotionProduct;
    }

    public int checkLackOfPromotion(int requestProductValue, Product promotionProduct) {
        return promotionProduct.checkLackOfPromotion(requestProductValue);
    }

    public int checkPromotionApplied(int requestProductValue, Product promotionProduct) {
        return promotionProduct.calculateRequiredPromotionQuantity(requestProductValue);
    }

    private boolean isPromotionProduct(String requestProductName, Product product) {
        return product.isSameName(requestProductName) && product.isPromotionProduct();
    }

    public List<Product> extractNoPromotionProducts(String requestProductName) {
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


    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
}