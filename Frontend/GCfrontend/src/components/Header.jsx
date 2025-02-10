import React from "react";
import { Link } from "react-router-dom";
import { FaShoppingCart } from "react-icons/fa"; // Import cart icon from react-icons

function Header({ cartCount }) {
  return (
    <header className="header">
      <div className="header-content">
        <h1>Glorious Creations</h1>
        <nav>
          <ul>
            <li><Link to="/">Home</Link></li>
            <li><Link to="/products">Products</Link></li>
            <li><Link to="/contact">Contact</Link></li>
            <li><Link to="/sign-up">Sign Up</Link></li> {/* Link to Sign Up page */}
            <li><Link to="/login">Login</Link></li>   {/* Link to Login page */}
          </ul>
        </nav>
        <div className="cart-icon">
          <Link to="/cart">
            <FaShoppingCart />
            {cartCount > 0 && <span className="cart-count">{cartCount}</span>}
          </Link>
        </div>
      </div>
    </header>
  );
}

export default Header;
