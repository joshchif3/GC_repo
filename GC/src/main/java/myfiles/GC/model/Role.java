package myfiles.GC.model;

import jakarta.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private UserRole name;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public UserRole getName() { return name; }
    public void setName(UserRole name) { this.name = name; }
}
