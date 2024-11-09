package store.service;

import store.application.ProductsDto;
import store.domain.Products;
import store.domain.Promotions;

public class ConvenienceStoreService {
    private final Promotions promotions = new Promotions("/promotions.md");
    private final Products products = new Products("/products.md", promotions);

    public ProductsDto loadInitialProducts() {
        return new ProductsDto(products.getProducts());
    }
}
