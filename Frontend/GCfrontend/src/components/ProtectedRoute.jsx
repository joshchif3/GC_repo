// src/components/ProtectedRoute.jsx
import React, { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../services/AuthContext.jsx";

function ProtectedRoute({ children, role }) {
  const { user } = useContext(AuthContext);

  if (!user) {
    return <Navigate to="/login" />; // Redirect to login if not authenticated
  }

  if (role && user.role !== role) {
    return <Navigate to="/" />; // Redirect to home if user doesn't have the required role
  }

  return children;
}

export default ProtectedRoute;