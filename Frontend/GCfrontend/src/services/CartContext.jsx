import React, { createContext, useState, useContext, useEffect } from "react";
import { fetchCart, addToCart as addToCartApi, removeFromCart as removeFromCartApi, updateQuantity as updateQuantityApi, fetchProductById } from "./api";

export const CartContext = createContext();

export const CartProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState({}); // Use an object to store cart items
  const [productDetails, setProductDetails] = useState({}); // Store product details including prices

  // Fetch the user's cart when the component mounts
  const fetchUserCart = async (cartId) => {
    try {
      const cart = await fetchCart(cartId);
      setCartItems(cart.items); // cart.items is an object like { "1": 4, "2": 1 }

      // Fetch product details for each item in the cart
      const productIds = Object.keys(cart.items);
      const productDetailsPromises = productIds.map(id => fetchProductById(id));
      const productDetailsResults = await Promise.all(productDetailsPromises);
      const productDetailsMap = productDetailsResults.reduce((acc, product) => {
        acc[product.id] = product;
        return acc;
      }, {});
      setProductDetails(productDetailsMap);
    } catch (error) {
      console.error("Failed to fetch cart:", error);
    }
  };

  // Add an item to the cart
  const addToCart = async (cartId, productId, quantity) => {
    try {
      const updatedCart = await addToCartApi(cartId, productId, quantity);
      setCartItems(updatedCart.items); // Update the cart items

      // Fetch the product details if not already in the state
      if (!productDetails[productId]) {
        const product = await fetchProductById(productId);
        setProductDetails(prevDetails => ({
          ...prevDetails,
          [productId]: product,
        }));
      }
    } catch (error) {
      console.error("Failed to add to cart:", error);
      throw error; // Re-throw the error to handle it in the component
    }
  };

  // Remove an item from the cart
  const removeFromCart = async (cartId, productId) => {
    try {
      const updatedCart = await removeFromCartApi(cartId, productId);
      setCartItems(updatedCart.items); // Update the cart items
    } catch (error) {
      console.error("Failed to remove from cart:", error);
    }
  };

  // Update the quantity of an item in the cart
  const updateQuantity = async (cartId, productId, quantity) => {
    try {
      const updatedCart = await updateQuantityApi(cartId, productId, quantity);
      setCartItems(updatedCart.items); // Update the cart items
    } catch (error) {
      console.error("Failed to update quantity:", error);
    }
  };

  return (
    <CartContext.Provider value={{ cartItems, productDetails, addToCart, removeFromCart, updateQuantity, fetchUserCart }}>
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => useContext(CartContext);