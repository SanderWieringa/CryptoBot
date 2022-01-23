package Rest.socketModels;

import com.binance.api.client.domain.account.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UpdateOrdersMessage {

    @Getter
    @Setter
    private MessageType type;
    @Getter
    @Setter
    private List<Order> content;

    public UpdateOrdersMessage() {

    }
}
