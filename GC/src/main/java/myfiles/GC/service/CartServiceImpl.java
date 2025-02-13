package myfiles.GC.service;

import myfiles.GC.model.Cart;
import myfiles.GC.model.Product;
import myfiles.GC.repository.CartRepo;
import myfiles.GC.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public Cart addToCart(int cartId, int productId, int quantity) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the product is in stock
        if (!productService.isProductInStock(productId)) {
            throw new RuntimeException("Product is out of stock");
        }

        // Check if the requested quantity exceeds the available stock
        if (product.getStockCount() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        // Update the cart
        cart.getItems().merge(productId, quantity, Integer::sum);

        // Reduce the product stock
        productService.updateStockCount(productId, false); // Decrease stock by 1 for each quantity

        return cartRepo.save(cart);
    }

    @Override
    @Transactional
    public Cart removeFromCart(int cartId, int productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().containsKey(productId)) {
            int removedQuantity = cart.getItems().get(productId);
            cart.getItems().remove(productId);

            // Restore the product stock
            productService.updateStockCount(productId, true); // Increase stock by the removed quantity
        }

        return cartRepo.save(cart);
    }

    @Override
    public Cart getCart(int cartId) {
        return cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    @Transactional
    public void clearCart(int cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Restore stock for all items in the cart
        cart.getItems().forEach((productId, quantity) -> {
            productService.updateStockCount(productId, true); // Increase stock by the quantity
        });

        cart.getItems().clear();
        cartRepo.save(cart);
    }

    @Override
    @Transactional
    public Cart updateQuantity(int cartId, int productId, int quantity) {
        Cart cart = getCart(cartId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the new quantity is valid
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        // Check if the product is in stock
        if (!productService.isProductInStock(productId)) {
            throw new RuntimeException("Product is out of stock");
        }

        // Check if the requested quantity exceeds the available stock
        if (product.getStockCount() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        // Update the cart
        cart.getItems().put(productId, quantity);

        // Adjust the product stock
        int oldQuantity = cart.getItems().getOrDefault(productId, 0);
        int delta = quantity - oldQuantity;
        if (delta > 0) {
            productService.updateStockCount(productId, false); // Decrease stock by the delta
        } else if (delta < 0) {
            productService.updateStockCount(productId, true); // Increase stock by the delta
        }

        return cartRepo.save(cart);
    }
}