package myfiles.GC.controller;

import myfiles.GC.model.Cart;
import myfiles.GC.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173") // Ensure frontend can access this API
public class CartController {

    @Autowired
    private CartService cartService;

    // Add a product to the cart
    @PreAuthorize("hasRole('USER')") // Restricting to users
    @PostMapping("/{cartId}/add")
    public ResponseEntity<Cart> addToCart(
            @PathVariable int cartId,
            @RequestParam int productId,
            @RequestParam int quantity) {
        Cart cart = cartService.addToCart(cartId, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    // Remove a product from the cart
    @PreAuthorize("hasRole('USER')") // Restricting to users
    @DeleteMapping("/{cartId}/remove")
    public ResponseEntity<Cart> removeFromCart(
            @PathVariable int cartId,
            @RequestParam int productId) {
        Cart cart = cartService.removeFromCart(cartId, productId);
        return ResponseEntity.ok(cart);
    }

    // Get the cart by ID
    @PreAuthorize("hasRole('USER')") // Restricting to users
    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable int cartId) {
        Cart cart = cartService.getCart(cartId);
        return ResponseEntity.ok(cart);
    }

    // Clear the cart
    @PreAuthorize("hasRole('USER')") // Restricting to users
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable int cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    // Update quantity of a product in the cart
    @PreAuthorize("hasRole('USER')") // Restricting to users
    @PutMapping("/{cartId}/update")
    public ResponseEntity<Cart> updateQuantity(
            @PathVariable int cartId,
            @RequestParam int productId,
            @RequestParam int quantity) {
        Cart cart = cartService.updateQuantity(cartId, productId, quantity);
        return ResponseEntity.ok(cart);
    }
}
