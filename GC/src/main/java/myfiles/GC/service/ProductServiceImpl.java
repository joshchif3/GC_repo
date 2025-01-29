package myfiles.GC.service;

import myfiles.GC.model.Product;
import myfiles.GC.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Product getProductById(int id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteProduct(int id) {
        productRepo.deleteById(id);
    }

    @Override
    public Product updateStockCount(int productId, boolean increaseStock) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        // Increment or decrement stock count
        if (increaseStock) {
            product.setStockCount(product.getStockCount() + 1);
        } else if (product.getStockCount() > 0) {
            product.setStockCount(product.getStockCount() - 1);
        } else {
            throw new RuntimeException("Stock count cannot be negative");
        }

        return productRepo.save(product);
    }

    @Override
    public List<Product> saveProducts(List<Product> products) {
        return productRepo.saveAll(products); // Save all products in one batch
    }
}
