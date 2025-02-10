import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom"; // useNavigate instead of useHistory
import "../styling/productform.css"; // Import your new CSS file

const ProductFormPage = () => {
    const [formData, setFormData] = useState({
        name: "",
        description: "",
        price: "",
        stockCount: "",
        imageUrl: "",
        category: "",
        status: "AVAILABLE",
    });
    const [editingProduct, setEditingProduct] = useState(false);
    const navigate = useNavigate(); // useNavigate instead of useHistory
    const { id } = useParams();

    const categories = [
        "HOODIES", "TSHIRTS", "CAPS", "BAGS", "SWEATERS", "JACKETS", "ACCESSORIES", "OTHER"
    ];

    const statuses = [
        "AVAILABLE", "OUT_OF_STOCK", "DISCONTINUED", "PENDING"
    ];

    useEffect(() => {
        if (id) {
            // Edit product mode
            fetchProduct(id);
        }
    }, [id]);

    const fetchProduct = async (id) => {
        try {
            const res = await fetch(`http://localhost:8080/api/products/${id}`);
            const product = await res.json();
            setFormData(product);
            setEditingProduct(true);
        } catch (error) {
            console.error("Error fetching product:", error);
        }
    };

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const method = editingProduct ? "PUT" : "POST";
        const url = editingProduct
            ? `http://localhost:8080/api/products/${formData.id}`
            : "http://localhost:8080/api/products";

        try {
            await fetch(url, {
                method,
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(formData),
            });
            navigate("/admin"); // Redirect to admin page after submission
        } catch (error) {
            console.error("Error saving product:", error);
        }
    };

    return (
        <div className="product-form-container">
            <h2 className="product-form-header">{editingProduct ? "Edit Product" : "Add Product"}</h2>

            <form className="product-form" onSubmit={handleSubmit}>
                <label>Product Name:</label>
                <input
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    required
                />

                <label>Description:</label>
                <textarea
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    required
                />

                <label>Price:</label>
                <input
                    type="number"
                    name="price"
                    value={formData.price}
                    onChange={handleChange}
                    required
                />

                <label>Stock Count:</label>
                <input
                    type="number"
                    name="stockCount"
                    value={formData.stockCount}
                    onChange={handleChange}
                    required
                />

                <label>Image URL:</label>
                <input
                    type="text"
                    name="imageUrl"
                    value={formData.imageUrl}
                    onChange={handleChange}
                    required
                />

                <label>Category:</label>
                <select
                    name="category"
                    value={formData.category}
                    onChange={handleChange}
                    required
                >
                    {categories.map((category) => (
                        <option key={category} value={category}>{category}</option>
                    ))}
                </select>

                <label>Status:</label>
                <select
                    name="status"
                    value={formData.status}
                    onChange={handleChange}
                    required
                >
                    {statuses.map((status) => (
                        <option key={status} value={status}>{status}</option>
                    ))}
                </select>

                <button type="submit">{editingProduct ? "Update Product" : "Add Product"}</button>
            </form>
        </div>
    );
};

export default ProductFormPage;
