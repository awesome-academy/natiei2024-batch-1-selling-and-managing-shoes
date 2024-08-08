package group1.intern.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.Type;
import group1.intern.model.Embeddables.ProductDescription;
import group1.intern.model.Enum.ProductGender;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
@NamedEntityGraph(
    name = "Product.styleAndDetails",
    attributeNodes = {
        @NamedAttributeNode("style"),
        @NamedAttributeNode("productDetails")
    }
)
public class Product extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int originPrice;
    @Column(columnDefinition = "integer default 0")
    private Integer discount;
    private String name;
    @Enumerated(EnumType.STRING)
    private ProductGender gender;
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private ProductDescription description;
    // One-to-Many relationship with ProductImage
    @OneToMany(mappedBy = "product")
    private List<ProductImage> images;
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Constant category;

    @ManyToOne
    @JoinColumn(name = "style_id")
    private Constant style;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Constant material;

    @OneToMany(mappedBy = "product")
    private List<ProductDetail> productDetails;

    public String getOriginPriceFormated() {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return formatter.format(this.originPrice) + " VND";
    }

    public String getDiscountPriceFormated() {
        if (discount == null || discount <= 0) return "";
        int discountedPrice = originPrice - (originPrice * discount / 100);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        return numberFormat.format(discountedPrice) + " VND";
    }

    public boolean isSoldOut() {
        return productDetails.stream()
            .allMatch(detail -> detail.getQuantity() <= 0);
    }

    public String getProductDetailUrl() {
        return "/product-detail/" + this.id;
    }
}