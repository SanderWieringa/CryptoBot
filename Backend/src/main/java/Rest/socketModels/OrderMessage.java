package Rest.socketModels;

import com.binance.api.client.domain.account.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class OrderMessage {

    @Getter
    private MessageType type;
    @Getter
    @Setter
    private Order content;
    @Getter
    private int sender;


}