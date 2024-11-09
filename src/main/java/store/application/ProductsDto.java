package store.application;

import store.domain.Product;
import store.domain.Promotion;

import java.util.ArrayList;
import java.util.List;

public class ProductsDto {
    private final List<ProductDto> products;

    public ProductsDto(List<Product> products) {
        this.products = createProductsDto(products);
    }

    private List<ProductDto> createProductsDto(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            String promotionName = product.getPromotion()
                    .map(Promotion::getPromotionName)
                    .orElse("");

            productDtos.add(new ProductDto(product.getName(), product.getPrice(), product.getQuantity().getQuantity(), promotionName));
        }
        return productDtos;
    }

    public List<ProductDto> getProducts() {
        return products;
    }
}
