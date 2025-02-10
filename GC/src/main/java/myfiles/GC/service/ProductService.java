package myfiles.GC.service;

import myfiles.GC.model.Product;
import myfiles.GC.model.ProductCategory;
import myfiles.GC.model.ProductStatus;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product saveProduct(Product product);
    Product getProductById(int id);
    void deleteProduct(int id);
    Product updateStockCount(int productId, boolean increaseStock);
    List<Product> saveProducts(List<Product> products);
    List<Product> getProductsByCategory(ProductCategory category);
    List<Product> getProductsByStatus(ProductStatus status);
    Product updateProduct(int id, Product productDetails);
    boolean isProductInStock(int productId); // Method for stock check
}
