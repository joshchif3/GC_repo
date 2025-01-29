package myfiles.GC.controller;

import myfiles.GC.model.Product;
import myfiles.GC.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")

public class ProductController {

    @Autowired
    private ProductService productService;

    // Retrieve all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Save a new product
    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Get a product by its ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    // Delete a product by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Update stock count for a product (increase or decrease)
    @PatchMapping("/{id}/stock")
    public Product updateStockCount(@PathVariable int id, @RequestParam boolean increaseStock) {
        return productService.updateStockCount(id, increaseStock);
    }

    // Save multiple products
    @PostMapping("/bulk")
    public List<Product> saveProducts(@RequestBody List<Product> products) {
        return productService.saveProducts(products);
    }


}
