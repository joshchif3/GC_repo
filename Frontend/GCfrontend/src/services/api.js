const API_URL = "http://localhost:8080/api";

// Fetch all products
export const fetchProducts = async () => {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}/products`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.json();
};

// Fetch a single product by ID
export const fetchProductById = async (id) => {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}/products/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.json();
};

// Create a new product
export const createProduct = async (product) => {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}/products`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(product),
  });
  return response.json();
};

// Update an existing product
export const updateProduct = async (id, product) => {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}/products/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(product),
  });
  return response.json();
};

// Delete a product
export const deleteProduct = async (id) => {
  const token = localStorage.getItem("token");
  await fetch(`${API_URL}/products/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Fetch the user's cart
export const fetchCart = async (cartId) => {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}/cart/${cartId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.json();
};

// Add an item to the cart
export const addToCart = async (cartId, productId, quantity) => {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}/cart/${cartId}/add?productId=${productId}&quantity=${quantity}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    const errorData = await response.text();
    throw new Error(`Failed to add to cart: ${errorData}`);
  }

  return response.json();
};

// Remove an item from the cart
export const removeFromCart = async (cartId, productId) => {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}/cart/${cartId}/remove?productId=${productId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    const errorData = await response.text();
    throw new Error(`Failed to remove from cart: ${errorData}`);
  }

  return response.json();
};

// Update the quantity of an item in the cart
export const updateQuantity = async (cartId, productId, quantity) => {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}/cart/${cartId}/update?productId=${productId}&quantity=${quantity}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    const errorData = await response.text();
    throw new Error(`Failed to update quantity: ${errorData}`);
  }

  return response.json();
};

// Create a payment intent for Stripe
export const createPaymentIntent = async (data) => {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}/checkout`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(data), // Send the amount and other necessary data
  });

  if (!response.ok) {
    const errorData = await response.text();
    throw new Error(`Failed to create payment intent: ${errorData}`);
  }

  return response.json();
};