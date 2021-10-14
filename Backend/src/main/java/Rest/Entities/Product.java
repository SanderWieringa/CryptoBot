package Rest.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Table(name = "Product")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @Column(unique=true, nullable=false)
    @Id
    private int id;
    @JsonProperty("fullName")
    private String name;
    @JsonProperty("price")
    private float price;
    @JsonProperty("marketCap")
    private long marketCap;

    public Product(int id, String name, float price, long marketCap) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.marketCap = marketCap;
    }

    public Product() {

    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name;}

    public void setName(String name) { this.name = name; }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getMarketCap() { return marketCap; }

    public void setMarketCap(long marketCap) { this.marketCap = marketCap; }
}
