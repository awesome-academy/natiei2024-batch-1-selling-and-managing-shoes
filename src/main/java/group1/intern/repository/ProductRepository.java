package group1.intern.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import group1.intern.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(value = "Product.styleAndDetails", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<Product> findProductByName(String name, Pageable pageable);
}