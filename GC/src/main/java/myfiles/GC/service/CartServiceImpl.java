package myfiles.GC.service;

import myfiles.GC.model.Cart;
import myfiles.GC.model.Product;
import myfiles.GC.repository.CartRepo;
import myfiles.GC.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Override
    public Cart addToCart(int cartId, int productId, int quantity) {
        Cart cart = cartRepo.findById(cartId).orElse(new Cart());
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update quantity if the product is already in the cart
        cart.getItems().merge(productId, quantity, Integer::sum);

        return cartRepo.save(cart);
    }

    @Override
    public Cart removeFromCart(int cartId, int productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().containsKey(productId)) {
            cart.getItems().remove(productId);
        }

        return cartRepo.save(cart);
    }

    @Override
    public Cart getCart(int cartId) {
        return cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public void clearCart(int cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getItems().clear();
        cartRepo.save(cart);
    }

    @Override
    public Cart updateQuantity(int cartId, int productId, int quantity) {
        Cart cart = getCart(cartId);
        cart.getItems().put(productId, quantity);
        return cartRepo.save(cart);
    }
}
