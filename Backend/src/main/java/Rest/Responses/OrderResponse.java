package Rest.Responses;

import com.binance.api.client.domain.account.Order;

import java.util.List;

public class OrderResponse {
    private boolean success;
    private List<List<Order>> orders;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public List<List<Order>> getOrders() { return orders; }
    public void setOrders(List<List<Order>> orders) { this.orders = orders;}
}
