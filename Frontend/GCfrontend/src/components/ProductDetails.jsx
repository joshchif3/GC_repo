import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ProductService from "../service/ProductService";
import AddToCartButton from "./AddToCartButton";

const ProductDetails = ({ addToCart }) => {
  const { id } = useParams(); // Get the product ID from the URL
  const [product, setProduct] = useState(null);

  useEffect(() => {
    ProductService.getProductById(id)
      .then((response) => setProduct(response.data))
      .catch((error) => console.error("Error fetching product details:", error));
  }, [id]);

  if (!product) {
    return <div>Loading...</div>;
  }

  return (
    <div className="product-details">
      <h2>{product.name}</h2>
      <img
        src={product.imageUrl || "https://via.placeholder.com/150"}
        alt={product.name}
      />
      <p>{product.description}</p>
      <p className="price">Price: ${product.price}</p>
      <p>Stock: {product.stockCount}</p>
      <p>Category: {product.category}</p>
      <p>Status: {product.status}</p>
      <AddToCartButton productId={product.id} addToCart={addToCart} />
    </div>
  );
};

export default ProductDetails;