import axios from "axios";

const API_URL = "http://localhost:8080/api/products"; // replace with your actual API URL

const getAllProducts = (token) => {
  return axios.get(API_URL, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export default {
  getAllProducts,
};
