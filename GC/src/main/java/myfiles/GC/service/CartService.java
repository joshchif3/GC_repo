package myfiles.GC.service;

import myfiles.GC.model.Cart;

public interface CartService {
    Cart addToCart(int cartId, int productId, int quantity);
    Cart removeFromCart(int cartId, int productId);
    Cart getCart(int cartId);
    void clearCart(int cartId);
    Cart updateQuantity(int cartId, int productId, int quantity); // Updated method
}
