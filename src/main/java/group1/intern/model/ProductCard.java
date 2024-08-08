package group1.intern.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCard {
    private Integer id;
    private String name;
    private String image1;
    private String image2;
    private String style;
    private String price;
    private boolean soldOut;
    private String url;
}

