package Rest.Entities;

import javax.persistence.*;

@Table(name = "Product")
@Entity
public class Product {
    @Column(unique=true, nullable=false)
    @Id
    private String id;

    private float price;

    public Product(String id, float price) {
        this.id = id;
        this.price = price;

    }

    public Product() {

    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
