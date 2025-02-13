import React, { useContext, useEffect } from "react";
import { CartContext } from "../services/CartContext";
import { useAuth } from "../services/AuthContext";

function Cart() {
  const { cartItems, removeFromCart, updateQuantity, fetchUserCart } = useContext(CartContext);
  const { user } = useAuth();

  useEffect(() => {
    if (user?.id) {
      fetchUserCart(user.id); // Use user.id as cartId
    }
  }, [user, fetchUserCart]);

  return (
    <div className="cart-container">
      <h2>Your Cart</h2>
      {Object.keys(cartItems).length === 0 ? (
        <p>Your cart is empty.</p>
      ) : (
        <div className="cart-items">
          {Object.entries(cartItems).map(([productId, quantity]) => (
            <div key={productId} className="cart-item">
              <h3>Product ID: {productId}</h3>
              <p>Quantity: {quantity}</p>
              <input
                type="number"
                value={quantity}
                onChange={(e) => updateQuantity(user.id, productId, parseInt(e.target.value))}
              />
              <button onClick={() => removeFromCart(user.id, productId)}>Remove</button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default Cart;