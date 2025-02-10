import React, { useState, useEffect } from "react";
import axios from "axios";
import AddToCartButton from "./AddToCartButton";
import { Link } from "react-router-dom";
import ProductService from "../service/ProductService";

const ProductList = ({ addToCart }) => {
  const [products, setProducts] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("ALL");
  const [filteredProducts, setFilteredProducts] = useState([]);
  const token = localStorage.getItem("authToken");

  useEffect(() => {
    if (token) {
      ProductService.getAllProducts(token)
        .then((response) => {
          console.log(response.data); // Log the response to check the structure
          if (Array.isArray(response.data)) {
            setProducts(response.data);
            setFilteredProducts(response.data);
          } else {
            console.error("Invalid response structure:", response.data);
          }
        })
        .catch((error) => console.error("Error fetching products:", error));
    } else {
      console.log("No token found. Please login.");
    }
  }, [token]);

  useEffect(() => {
    filterProducts();
  }, [searchTerm, selectedCategory, products]);

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };

  const handleCategoryChange = (e) => {
    setSelectedCategory(e.target.value);
  };

  const filterProducts = () => {
    let filtered = [...products]; // Copy the products array

    if (searchTerm) {
      filtered = filtered.filter(
        (product) =>
          product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          product.description.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (selectedCategory && selectedCategory !== "ALL") {
      filtered = filtered.filter((product) => product.category === selectedCategory);
    }

    setFilteredProducts(filtered);
  };

  return (
    <div className="product-list">
      <h2>Our Products</h2>
      <div className="filter-container">
        <input
          type="text"
          placeholder="Search products..."
          value={searchTerm}
          onChange={handleSearchChange}
        />
        <select value={selectedCategory} onChange={handleCategoryChange}>
          <option value="ALL">All Categories</option>
          <option value="HOODIES">Hoodies</option>
          <option value="TSHIRTS">T-shirts</option>
          <option value="CAPS">Caps</option>
          <option value="BAGS">Bags</option>
          <option value="SWEATERS">Sweaters</option>
          <option value="JACKETS">Jackets</option>
          <option value="ACCESSORIES">Accessories</option>
          <option value="OTHER">Other</option>
        </select>
      </div>

      <div className="products">
        {filteredProducts.length === 0 ? (
          <p>No products found matching your criteria.</p>
        ) : (
          filteredProducts.map((product) => (
            <div key={product.id} className="product-card">
              <img
                src={product.imageUrl || "https://via.placeholder.com/150"}
                alt={product.name}
              />
              <h3>{product.name}</h3>
              <p>{product.description}</p>
              <p className="price">Price: ${product.price}</p>
              <p>Stock: {product.stockCount}</p>
              <p>
                <AddToCartButton productId={product.id} addToCart={addToCart} />
              </p>
              <Link to={`/products/${product.id}`} className="see-more-link">
                See More
              </Link>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default ProductList;
