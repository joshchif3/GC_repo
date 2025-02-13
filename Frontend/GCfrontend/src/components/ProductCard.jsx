// components/ProductCard.js
import React from "react";
import { Link } from "react-router-dom";

function ProductCard({ product }) {
  return (
    <div className="product-card">
      <img src={product.imageUrl} alt={product.name} />
      <h3>{product.name}</h3>
      <p>{product.description}</p>
      <p className="price">${product.price.toFixed(2)}</p>
      <Link to={`/products/${product.id}`} className="btn">View Details</Link>
    </div>
  );
}

export default ProductCard;