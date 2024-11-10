package store.application;

import store.domain.Product;
import store.domain.Promotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsDto {
    private final Map<String, List<ProductDto>> products;

    public ProductsDto(Map<String, List<Product>> products) {
        this.products = createProductsDto(products);
    }

    private Map<String, List<ProductDto>> createProductsDto(Map<String, List<Product>> products) {
        Map<String, List<ProductDto>> productDtos = new HashMap<>();
        for (Map.Entry<String, List<Product>> product : products.entrySet()) {
            List<Product> sameNameProducts = product.getValue();
            createProductDto(productDtos, sameNameProducts);
        }
        return productDtos;
    }

    private void createProductDto(Map<String, List<ProductDto>> productDtos, List<Product> sameNameProducts) {
        for (Product sameNameProduct : sameNameProducts) {
            String promotionName = sameNameProduct.getPromotion()
                    .map(Promotion::getPromotionName)
                    .orElse("");
            List<ProductDto> productDto = productDtos.getOrDefault(sameNameProduct.getName(), new ArrayList<>());
            productDto.add(new ProductDto(sameNameProduct.getName(), sameNameProduct.getPrice(), sameNameProduct.getQuantity().getQuantity(), promotionName));
            productDtos.put(sameNameProduct.getName(), productDto);
        }
    }

    public Map<String, List<ProductDto>> getProducts() {
        return products;
    }
}
