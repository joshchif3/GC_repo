package myfiles.GC.model;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:5173")
@Entity
@Table(name = "products")

public class Product {

    @Id
    private int id;

    private String name;
    private String description;
    private String category;
    private double price;
    private int stockCount;
    private int maxQuantity;
    private boolean discounted;

    @Version
    private int version;  // Version field for optimistic locking

    // getters and setters
}
