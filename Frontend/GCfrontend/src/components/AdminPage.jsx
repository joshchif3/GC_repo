import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../styling/admin.css";

const AdminPage = () => {
    const [products, setProducts] = useState([]);
    const [filter, setFilter] = useState({
        category: "",
        status: "",
        search: "",
    });
    const [sortOrder, setSortOrder] = useState("asc");
    const navigate = useNavigate();

    useEffect(() => {
        const userRole = localStorage.getItem("userRole");

        if (!userRole || userRole !== "ADMIN") {
            navigate("/products"); // Redirect if not an admin
            return;
        }

        fetchProducts();
    }, [navigate]);

    const fetchProducts = async () => {
        try {
            const res = await fetch("http://localhost:8080/api/products");
            const data = await res.json();
            setProducts(data);
        } catch (error) {
            console.error("Error fetching products:", error);
        }
    };

    const handleFilterChange = (e) => {
        setFilter({
            ...filter,
            [e.target.name]: e.target.value
        });
    };

    const handleSearch = (e) => {
        setFilter({
            ...filter,
            search: e.target.value
        });
    };

    const handleSort = () => {
        setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    };

    const filteredProducts = products.filter((product) => {
        return (
            (filter.category ? product.category === filter.category : true) &&
            (filter.status ? product.status === filter.status : true) &&
            (filter.search ? product.name.toLowerCase().includes(filter.search.toLowerCase()) : true)
        );
    });

    const sortedProducts = filteredProducts.sort((a, b) => {
        return sortOrder === "asc" ? a.name.localeCompare(b.name) : b.name.localeCompare(a.name);
    });

    return (
        <div className="admin-container">
            <h2 className="admin-header">Admin Page</h2>

            <Link to="/product-form">
                <button className="add-product-btn">Add New Product</button>
            </Link>

            <div className="filters">
                <div className="filter-group">
                    <label>Search:</label>
                    <input type="text" name="search" placeholder="Search by name" value={filter.search} onChange={handleSearch} />
                </div>

                <div className="filter-group">
                    <label>Category:</label>
                    <select name="category" value={filter.category} onChange={handleFilterChange}>
                        <option value="">All Categories</option>
                        <option value="HOODIES">HOODIES</option>
                        <option value="TSHIRTS">TSHIRTS</option>
                        <option value="CAPS">CAPS</option>
                        <option value="BAGS">BAGS</option>
                        <option value="SWEATERS">SWEATERS</option>
                        <option value="JACKETS">JACKETS</option>
                        <option value="ACCESSORIES">ACCESSORIES</option>
                        <option value="OTHER">OTHER</option>
                    </select>
                </div>

                <div className="filter-group">
                    <label>Status:</label>
                    <select name="status" value={filter.status} onChange={handleFilterChange}>
                        <option value="">All Statuses</option>
                        <option value="AVAILABLE">AVAILABLE</option>
                        <option value="OUT_OF_STOCK">OUT_OF_STOCK</option>
                        <option value="DISCONTINUED">DISCONTINUED</option>
                        <option value="PENDING">PENDING</option>
                    </select>
                </div>

                <button className="sort-btn" onClick={handleSort}>Sort by Name</button>
            </div>

            <h3 className="admin-header">Product List</h3>
            <div className="admin-product-list">
                <table className="admin-product-table">
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Category</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {sortedProducts.map((product) => (
                            <tr key={product.id}>
                                <td><img src={product.imageUrl} alt={product.name} width="50" /></td>
                                <td>{product.name}</td>
                                <td>${product.price}</td>
                                <td>{product.category}</td>
                                <td>{product.status}</td>
                                <td className="admin-actions">
                                    <Link to={`/product-form/${product.id}`}>
                                        <button className="edit-btn">Edit</button>
                                    </Link>
                                    <button
                                        className="delete-btn"
                                        onClick={async () => {
                                            try {
                                                await fetch(`http://localhost:8080/api/products/${product.id}`, { method: "DELETE" });
                                                fetchProducts();
                                            } catch (error) {
                                                console.error("Error deleting product:", error);
                                            }
                                        }}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default AdminPage;
