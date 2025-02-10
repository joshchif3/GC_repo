package myfiles.GC.service;

import myfiles.GC.exception.ResourceNotFoundException;
import myfiles.GC.model.Product;
import myfiles.GC.model.ProductCategory;
import myfiles.GC.model.ProductStatus;
import myfiles.GC.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.AVAILABLE);
        }
        return productRepo.save(product);
    }

    @Override
    public Product getProductById(int id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public void deleteProduct(int id) {
        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepo.deleteById(id);
    }

    @Override
    @Transactional
    public Product updateStockCount(int productId, boolean increaseStock) {
        Product product = getProductById(productId);

        if (increaseStock) {
            product.setStockCount(product.getStockCount() + 1);
        } else {
            if (product.getStockCount() <= 0) {
                throw new IllegalStateException("Stock count cannot be negative");
            }
            product.setStockCount(product.getStockCount() - 1);
        }

        if (product.getStockCount() <= 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        } else if (product.getStatus() == ProductStatus.OUT_OF_STOCK) {
            product.setStatus(ProductStatus.AVAILABLE);
        }

        return productRepo.save(product);
    }

    @Override
    public List<Product> saveProducts(List<Product> products) {
        products.forEach(product -> {
            if (product.getStatus() == null) {
                product.setStatus(ProductStatus.AVAILABLE);
            }
        });
        return productRepo.saveAll(products);
    }

    @Override
    public List<Product> getProductsByCategory(ProductCategory category) {
        return productRepo.findByCategory(category);
    }

    @Override
    public List<Product> getProductsByStatus(ProductStatus status) {
        return productRepo.findByStatus(status);
    }

    @Override
    public Product updateProduct(int id, Product productDetails) {
        Product product = getProductById(id);

        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
        }
        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }
        if (productDetails.getCategory() != null) {
            product.setCategory(productDetails.getCategory());
        }
        if (productDetails.getPrice() > 0) {
            product.setPrice(productDetails.getPrice());
        }
        if (productDetails.getStockCount() >= 0) {
            product.setStockCount(productDetails.getStockCount());
        }
        if (productDetails.getMaxQuantity() > 0) {
            product.setMaxQuantity(productDetails.getMaxQuantity());
        }
        if (productDetails.getStatus() != null) {
            product.setStatus(productDetails.getStatus());
        }
        if (productDetails.getImageUrl() != null) {
            product.setImageUrl(productDetails.getImageUrl());
        }

        return productRepo.save(product);
    }

    @Override
    public boolean isProductInStock(int productId) {
        Product product = getProductById(productId);
        return product.getStockCount() > 0;
    }
}
