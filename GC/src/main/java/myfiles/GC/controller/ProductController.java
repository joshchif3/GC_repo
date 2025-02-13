package myfiles.GC.controller;

import myfiles.GC.model.Product;
import myfiles.GC.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Retrieve all products (accessible by everyone)
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Save a new product (restricted to ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Get a product by its ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    // Delete a product by its ID (restricted to ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Update product details (restricted to ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product productDetails) {
        return productService.updateProduct(id, productDetails);
    }

    // Update stock count for a product (restricted to ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/stock")
    public Product updateStockCount(@PathVariable int id, @RequestParam boolean increaseStock) {
        return productService.updateStockCount(id, increaseStock);
    }

    // Save multiple products (restricted to ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/bulk")
    public List<Product> saveProducts(@RequestBody List<Product> products) {
        return productService.saveProducts(products);
    }
}