package Rest.Controllers;

import Rest.Entities.Product;
import Rest.Services.CandleCollectionService;
import Rest.Services.ClientCreatorService;
import Rest.Services.UserService;
import Rest.socketModels.MarginMessage;
import Rest.socketModels.MessageType;
import Rest.socketModels.OrderMessage;
import Rest.socketModels.UpdateOrdersMessage;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.request.AllOrdersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@Transactional
public class OrderController {

    @Autowired
    private SimpMessageSendingOperations sendingOperations;

    private int userId;

    @Autowired
    private CandleCollectionService candleCollectionService;

    @Autowired
    private UserService userService;

    private final ClientCreatorService clientCreatorService = new ClientCreatorService();

    private final BinanceApiRestClient client = clientCreatorService.createBinanceApiRestClient();

    @MessageMapping("/orders.send")
    @SendTo("/topic/public")
    public void sendMessage(@Payload final MarginMessage marginMessage) {
        candleCollectionService.setQuantity(marginMessage.getQuantity());
    }

    @MessageMapping("/orders.newUser")
    @SendTo("/topic/public")
    public OrderMessage newUser(@Payload final OrderMessage orderMessage, SimpMessageHeaderAccessor headerAccessor) {
        setUserId(orderMessage.getSender());
        List<Order> allOrders = new ArrayList<>();
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("userId", orderMessage.getSender());
        List<Product> userProducts = userService.getUserProducts(orderMessage.getSender());
        for (Product product : userProducts) {
            List<Order> allProductOrders = client.getAllOrders(new AllOrdersRequest(product.getSymbol()));
            allOrders.addAll(allProductOrders);
        }
        orderMessage.setContent(allOrders);
        return orderMessage;
    }

    public void updateOrders() {
        List<Order> allOrders = new ArrayList<>();
        UpdateOrdersMessage updateOrdersMessage = new UpdateOrdersMessage();
        List<Product> userProducts = userService.getUserProducts(getUserId());

        for (Product product : userProducts) {
            List<Order> allProductOrders = client.getAllOrders(new AllOrdersRequest(product.getSymbol()));
            allOrders.addAll(allProductOrders);
        }
        updateOrdersMessage.setContent(allOrders);
        updateOrdersMessage.setType(MessageType.UPDATE);

        sendingOperations.convertAndSend("/topic/public", updateOrdersMessage);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
