package Rest.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Table(name = "Product")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @Column(unique=true, nullable=false)
    @Id
    private int id;
    @JsonProperty("fullName")
    @Column(nullable=false)
    private String name;
    @JsonProperty("price")
    @Column(nullable=false)
    private float price;
    @JsonProperty("marketCap")
    @Column
    private long marketCap;
    @JsonProperty("symbol")
    @Column(nullable=false)
    private String symbol;

    public Product(int id, String name, float price, long marketCap, String symbol) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.marketCap = marketCap;
        this.symbol = symbol;
    }

    public Product() {

    }

    @Override
    public String toString() {
        return (new ToStringBuilder("id: " + this.id)).append("symbol: " + this.symbol).toString();
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
