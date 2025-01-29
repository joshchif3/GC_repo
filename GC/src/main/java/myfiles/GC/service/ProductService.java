package myfiles.GC.service;

import myfiles.GC.model.Product;
import java.util.List;

public interface ProductService {
    // Retrieve all products
    List<Product> getAllProducts();

    // Save a single product
    Product saveProduct(Product product);

    // Get a product by its ID
    Product getProductById(int id);

    // Delete a product by its ID
    void deleteProduct(int id);

    // Update stock count for a product (increase or decrease)
    Product updateStockCount(int productId, boolean increaseStock);

    // Save multiple products
    List<Product> saveProducts(List<Product> products);
}
