package myfiles.GC.repository;

import myfiles.GC.model.Product;
import myfiles.GC.model.ProductCategory;
import myfiles.GC.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findByCategory(ProductCategory category);
    List<Product> findByStatus(ProductStatus status);
}