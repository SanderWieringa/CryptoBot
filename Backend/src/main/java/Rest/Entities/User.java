package Rest.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "profile")
@Entity
public class User {
    @Column(unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int userId;
    @Column(unique=true, nullable=false)
    private String username;
    @Column(nullable=false)
    private String password;
    @ManyToMany
    private List<Symbol> coinsToTradeIn = new ArrayList<>();

    public void setCoinsToTradeIn(List<Symbol> coinsToTradeIn) {
        this.coinsToTradeIn = coinsToTradeIn;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Symbol> getCoinsToTradeIn() {
        return coinsToTradeIn;
    }
}
