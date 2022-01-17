package Rest.Responses;

import com.binance.api.client.domain.account.Order;

import java.util.List;

public class PlaceOrderResponse {
    private boolean success;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
