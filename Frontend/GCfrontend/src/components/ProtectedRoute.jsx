import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../services/AuthContext';

function ProtectedRoute({ children, role }) {
  const { user, loading } = useAuth();

  if (loading) {
    // Show loading spinner or skeleton screen while checking auth
    return <div className="loading-screen">Loading...</div>;
  }

  if (!user) {
    // User not logged in, redirect to login
    return <Navigate to="/login" replace />;
  }

  if (role && user.role !== role) {
    // User doesn't have required role, redirect to home
    return <Navigate to="/" replace />;
  }

  // User is authenticated and has required role, render children
  return children;
}

export default ProtectedRoute;