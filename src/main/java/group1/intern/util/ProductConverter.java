package group1.intern.util;


import group1.intern.model.Product;
import group1.intern.model.ProductCard;
import group1.intern.model.ProductDetail;

import java.util.Optional;

public class ProductConverter {

    public static ProductCard convertToProductCard(Product product) {
        // Get the main image URL
        String mainImageUrl = product.getImages().stream()
            .findFirst()
            .map(image -> image.getUrl())
            .orElse("");

        // Get the secondary image URL
        String secondaryImageUrl = product.getImages().stream()
            .skip(1)
            .findFirst()
            .map(image -> image.getUrl())
            .orElse("");

        // Format price
        String price = String.format("%,d VND", product.getProductDetails().stream()
            .mapToInt(ProductDetail::getPrice)
            .findFirst()
            .orElse(0));

        // Determine if the product is sold out
        boolean soldOut = product.getProductDetails().stream()
            .allMatch(detail -> detail.getQuantity() <= 0);

        // Get the style name
        String style = Optional.ofNullable(product.getStyle()).map(name -> name.getType()).orElse("");


        // Build and return the ProductCard
        return new ProductCard(
            product.getId(),
            product.getName(),
            mainImageUrl,
            secondaryImageUrl,
            style,
            price,
            soldOut,
            "/product-detail/" + product.getId()
        );
    }
}

