import React from "react";

const AddToCartButton = ({ productId, addToCart }) => {
  const handleClick = () => {
    addToCart(productId);
  };

  return (
    <button onClick={handleClick}>Add to Cart</button>
  );
};

export default AddToCartButton;
