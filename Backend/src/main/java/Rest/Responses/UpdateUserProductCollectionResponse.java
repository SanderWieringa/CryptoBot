package Rest.Responses;

import Rest.Entities.Product;

import java.util.List;

public class UpdateUserProductCollectionResponse {
    private boolean isSuccess;
    private List<Product> coinsToTradeIn;
    private String username;
    private String password;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public List<Product> getCoinsToTradeIn() {
        return coinsToTradeIn;
    }

    public void setCoinsToTradeIn(List<Product> coinsToTradeIn) {
        this.coinsToTradeIn = coinsToTradeIn;
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
}
