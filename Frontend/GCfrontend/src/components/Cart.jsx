import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Cart = () => {
  const [cart, setCart] = useState(null);
  const [products, setProducts] = useState({});
  const cartId = 1; // Replace with dynamic cart ID
  const navigate = useNavigate();
  const token = localStorage.getItem("authToken");

  useEffect(() => {
    if (token) {
      axios
        .get(`http://localhost:8080/api/cart/${cartId}`, {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then((response) => {
          setCart(response.data);
          fetchProductDetails(response.data.items);
        })
        .catch((error) => console.error("Error fetching cart:", error));
    } else {
      console.log("No token found. Please login.");
    }
  }, [token]);

  const fetchProductDetails = async (cartItems) => {
    try {
      const productIds = Object.keys(cartItems); // Get product IDs from cart
      const productRequests = productIds.map((productId) =>
        axios.get(`http://localhost:8080/api/products/${productId}`, {
          headers: { Authorization: `Bearer ${token}` },
        })
      );

      const responses = await Promise.all(productRequests);
      const productData = responses.reduce((acc, res) => {
        acc[res.data.id] = res.data;
        return acc;
      }, {});

      setProducts(productData);
    } catch (error) {
      console.error("Error fetching product details:", error);
    }
  };

  const updateQuantity = (productId, newQuantity) => {
    axios
      .put(`http://localhost:8080/api/cart/${cartId}/update`, null, {
        headers: { Authorization: `Bearer ${token}` },
        params: {
          productId: productId,
          quantity: newQuantity,
        },
      })
      .then((response) => setCart(response.data))
      .catch((error) => console.error("Error updating quantity:", error));
  };

  const removeFromCart = (productId) => {
    axios
      .delete(`http://localhost:8080/api/cart/${cartId}/remove?productId=${productId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => setCart(response.data))
      .catch((error) => console.error("Error removing product from cart:", error));
  };

  const clearCart = () => {
    if (window.confirm("Are you sure you want to clear the entire cart?")) {
      axios
        .delete(`http://localhost:8080/api/cart/${cartId}/clear`, {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then(() => setCart({ items: {} }))
        .catch((error) => console.error("Error clearing the cart:", error));
    }
  };

  const getTotal = () => {
    return Object.entries(cart.items).reduce((total, [productId, quantity]) => {
      const product = products[productId];
      return total + (product ? product.price * quantity : 0);
    }, 0).toFixed(3);
  };

  if (!cart) return <div>Loading cart...</div>;

  return (
    <div className="cart-container">
      <h2>Your Cart</h2>
      {Object.keys(cart.items).length === 0 ? (
        <p>Your cart is empty.</p>
      ) : (
        <div className="cart-items">
          {Object.entries(cart.items).map(([productId, quantity], index) => {
            const product = products[productId];

            if (!product) return null;

            return (
              <div key={index} className="cart-item">
                <img
                  src={product.imageUrl || "https://via.placeholder.com/80"}
                  alt={product.name}
                />
                <div>
                  <h3
                    className="product-link"
                    onClick={() => navigate(`/products/${product.id}`)}
                  >
                    {product.name}
                  </h3>
                  <p>Price: ${product.price}</p>
                  <div>
                    <button
                      className="increase-quantity-btn"
                      onClick={() => updateQuantity(product.id, quantity + 1)}
                    >
                      +
                    </button>
                    <input
                      type="number"
                      value={quantity}
                      min="1"
                      max={product.stockCount}
                      onChange={(e) =>
                        updateQuantity(product.id, parseInt(e.target.value, 10))
                      }
                    />
                    <span> = ${(product.price * quantity).toFixed(2)}</span>
                  </div>
                  <button onClick={() => removeFromCart(product.id)}>Remove</button>
                </div>
              </div>
            );
          })}
        </div>
      )}
      <div className="cart-summary">
        <p>Total: ${getTotal()}</p>
        <button className="clear-cart-btn" onClick={clearCart}>
          Clear Cart
        </button>
      </div>
    </div>
  );
};

export default Cart;
