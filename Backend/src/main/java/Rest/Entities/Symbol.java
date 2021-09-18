package Rest.Entities;

import javax.persistence.*;

@Table(name = "Symbol")
@Entity
public class Symbol {
    @Column(unique=true, nullable=false)
    @Id
    private String id;

    private String symbol;

    public Symbol(String id, String symbol) {
        this.id = id;
        this.symbol = symbol;

    }

    public Symbol() {

    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
