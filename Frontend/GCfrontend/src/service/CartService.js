import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api/products', // Backend URL
});

export const fetchProducts = async () => {
  const response = await API.get('/');
  return response.data;
};

export const fetchProductById = async (id) => {
  const response = await API.get(`/${id}`);
  return response.data;
};

export const addProductToCart = async (product) => {
  // Logic to add product to cart, update the backend or local state
};

export const removeProductFromCart = async (productId) => {
  // Logic to remove product from cart, update the backend or local state
};
