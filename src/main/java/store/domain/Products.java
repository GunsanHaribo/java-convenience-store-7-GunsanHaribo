package store.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Products {
    private final Map<String, List<Product>> products;

    public Products(String filePath, Promotions promotions) {
        this.products = createProducts(filePath, promotions);
    }

    // TODO: 11/10/24 10줄
    private Map<String, List<Product>> createProducts(String filePath, Promotions promotions) {
        Map<String, List<Product>> products = new HashMap<>();
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
                List<Product> sameNameProducts = products.getOrDefault(name, new ArrayList<>());
                sameNameProducts.add(new Product(name, price, quantity, promotion));
                products.put(name, sameNameProducts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Optional<Product> findPromotionProducts(String requestProductName) {
        List<Product> sameNameProducts = products.get(requestProductName);
        return sameNameProducts.stream()
                .filter((Product::isPromotionProduct))
                .findFirst();
    }

    public List<Product> findNoPromotionProducts(String requestProductName) {
        List<Product> sameNameProducts = products.get(requestProductName);
        return sameNameProducts.stream()
                .filter(product -> !product.isPromotionProduct())
                .toList();
    }

    public Map<String, List<Product>> getProducts() {
        return Collections.unmodifiableMap(products);
    }
}