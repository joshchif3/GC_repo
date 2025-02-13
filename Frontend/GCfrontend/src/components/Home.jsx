// components/Home.js
import React from "react";
import { Link } from "react-router-dom";

function Home() {
  return (
    <div className="home">
      <div className="hero">
        <h2>Welcome to Glorious Creations</h2>
        <p>Faith-inspired fashion for everyone. Explore our collection of unique, high-quality products.</p>
        <Link to="/products" className="btn">Shop Now</Link>
      </div>
      <div className="features">
        <div className="feature">
          <h3>High-Quality Materials</h3>
          <p>Our products are made with the finest materials to ensure durability and comfort.</p>
        </div>
        <div className="feature">
          <h3>Unique Designs</h3>
          <p>Each product is designed with faith-inspired themes to reflect your beliefs.</p>
        </div>
        <div className="feature">
          <h3>Fast Shipping</h3>
          <p>We offer fast and reliable shipping to get your products to you as quickly as possible.</p>
        </div>
      </div>
    </div>
  );
}

export default Home;