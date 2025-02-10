import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./styling/main.css";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Home from "./components/Home";
import ProductList from "./components/ProductList";
import Contact from "./components/Contact";
import ProductDetails from "./components/ProductDetails";
import Cart from "./components/Cart";
import AdminPage from "./components/AdminPage";
import ProductFormPage from "./components/ProductFormPage";
import SignUpPage from "./components/SignUpPage";
import LoginPage from "./components/LoginPage";

function App() {
  const [cartItems, setCartItems] = useState([]);

  const addToCart = async (productId, quantity) => {
    try {
      const response = await fetch(`/api/products/${productId}`);
      if (!response.ok) {
        throw new Error('Product not found');
      }
      const product = await response.json();

      const existingItem = cartItems.find((item) => item.product.id === productId);

      if (existingItem) {
        setCartItems(
          cartItems.map((item) =>
            item.product.id === productId
              ? { ...item, quantity: item.quantity + quantity }
              : item
          )
        );
      } else {
        setCartItems([...cartItems, { product, quantity }]);
      }
    } catch (error) {
      console.error("Error adding product to cart:", error);
    }
  };

  return (
    <Router>
      <div className="app-container">
        <Header cartCount={cartItems.length} /> {/* Pass cart count */}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/products" element={<ProductList addToCart={addToCart} />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/products/:id" element={<ProductDetails addToCart={addToCart} />} />
          <Route path="/cart" element={<Cart cartItems={cartItems} />} />
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/product-form" element={<ProductFormPage />} />
          <Route path="/sign-up" element={<SignUpPage />} />
          <Route path="/login" element={<LoginPage />} />
        </Routes>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
