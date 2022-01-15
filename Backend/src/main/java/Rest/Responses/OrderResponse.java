package Rest.Responses;

import com.binance.api.client.domain.account.Order;

import java.util.List;

public class OrderResponse {
    private boolean success;
    private List<Order> orders;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders;}
}
