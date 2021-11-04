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
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Product> coinsToTradeIn = new ArrayList<>();

    public User() {

    }

    public User(int userId, String username, String password, List<Product> coinsToTradeIn) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.coinsToTradeIn = coinsToTradeIn;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", coinsToTradeIn=" + coinsToTradeIn +
                '}';
    }

    public void setCoinsToTradeIn(List<Product> coinsToTradeIn) {
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

    public List<Product> getCoinsToTradeIn() {
        return coinsToTradeIn;
    }
}
