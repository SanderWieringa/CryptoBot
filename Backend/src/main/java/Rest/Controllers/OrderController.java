package Rest.Controllers;

import Rest.Entities.Product;
import Rest.Services.CandleCollectionService;
import Rest.Services.ClientCreatorService;
import Rest.Services.IObserver;
import Rest.Services.UserService;
import Rest.socketModels.MarginMessage;
import Rest.socketModels.MessageType;
import Rest.socketModels.OrderMessage;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.request.AllOrdersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@Transactional
public class OrderController implements IObserver{

    private List<Order> allOrders;

    @Autowired
    private CandleCollectionService candleCollectionService;

    @Autowired
    private UserService userService;

    private final ClientCreatorService clientCreatorService = new ClientCreatorService();

    private final BinanceApiRestClient client = clientCreatorService.createBinanceApiRestClient();

    private List<Product> userProducts;

    private int userId;

    private OrderMessage orderMessage;

    @Autowired
    private BinanceController binanceController;

    @MessageMapping("/orders.send")
    @SendTo("/topic/public")
    public void sendMessage(@Payload final MarginMessage marginMessage) {
        System.out.println("marginMessage.price: " + marginMessage.getQuantity());
        candleCollectionService.setQuantity(marginMessage.getQuantity());
    }

    @MessageMapping("/orders.newUser")
    @SendTo("/topic/public")
    public OrderMessage newUser(@Payload final OrderMessage orderMessage, SimpMessageHeaderAccessor headerAccessor) {
        List<Order> allOrders = new ArrayList<>();
        System.out.println("orderMessage.getSender: " + orderMessage.getSender());
        candleCollectionService.subscribe(this);
        binanceController.subscribe(this);

        List<Product> userProducts = userService.getUserProducts(orderMessage.getSender());

        for (Product product : userProducts) {
            List<Order> allProductOrders = client.getAllOrders(new AllOrdersRequest(product.getSymbol()));
            allOrders.addAll(allProductOrders);
        }

        setAllOrders(allOrders);

        setUserId(orderMessage.getSender());

        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("userId", orderMessage.getSender());

        orderMessage.setContent(getAllOrders());
        return orderMessage;
    }

    @SendTo("/topic/public")
    public OrderMessage sendUpdatedMessage() {
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setSender(getUserId());
        orderMessage.setContent(getAllOrders());
        orderMessage.setType(MessageType.UPDATE);
        return orderMessage;
    }

    @Override
    public void update() {
        List<Product> userProducts = userService.getUserProducts(getUserId());
        for (Product product : userProducts) {
            List<Order> allProductOrders = client.getAllOrders(new AllOrdersRequest(product.getSymbol()));
            setAllOrders(allProductOrders);
        }
        sendUpdatedMessage();
    }

    public List<Order> getAllOrders() {
        return allOrders;
    }

    public void setAllOrders(List<Order> allOrders) {
        this.allOrders = allOrders;
    }

    public List<Product> getUserProducts() {
        return userProducts;
    }

    public void setUserProducts(List<Product> userProducts) {
        this.userProducts = userProducts;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public OrderMessage getOrderMessage() {
        return orderMessage;
    }

    public void setOrderMessage(OrderMessage orderMessage) {
        this.orderMessage = orderMessage;
    }
}
