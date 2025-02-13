import React, { createContext, useState, useContext } from "react";
import { login as loginApi, register as registerApi } from "./authService";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  const login = async (username, password) => {
    try {
      const response = await loginApi(username, password);
      const token = response.token;
      const role = response.role;
      const userId = response.userId; // Ensure the backend returns userId
      setUser({ id: userId, username, token, role, cart: { id: userId } }); // Use userId as cartId
      localStorage.setItem("token", token);
      localStorage.setItem("role", role);
      localStorage.setItem("userId", userId); // Store userId in localStorage
      return { token, role, userId };
    } catch (error) {
      console.error("Login failed:", error);
      throw error;
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userId");
  };

  const register = async (username, password, role) => {
    try {
      const response = await registerApi(username, password, role);
      return response;
    } catch (error) {
      console.error("Registration failed:", error);
      throw error;
    }
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, register }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);