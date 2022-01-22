package Rest.Controllers;

import Rest.Entities.Product;
import Rest.Entities.User;
import Rest.Responses.OrderResponse;
import Rest.Services.CandleCollectionService;
import Rest.Services.ClientCreatorService;
import Rest.Services.IObserver;
import Rest.Services.UserService;
import Rest.socketModels.MarginMessage;
import Rest.socketModels.OrderMessage;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.request.AllOrdersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@Transactional
public class OrderController {

    @Autowired
    private CandleCollectionService candleCollectionService;

    @Autowired
    private UserService userService;

    private final ClientCreatorService clientCreatorService = new ClientCreatorService();

    private final BinanceApiRestClient client = clientCreatorService.createBinanceApiRestClient();

    @MessageMapping("/orders.send")
    @SendTo("/topic/public")
    public void sendMessage(@Payload final MarginMessage marginMessage) {
        System.out.println("marginMessage.price: " + marginMessage.getQuantity());
        candleCollectionService.setQuantity(marginMessage.getQuantity());
    }

    @MessageMapping("/orders.newUser")
    @SendTo("/topic/public")
    public OrderMessage newUser(@Payload final OrderMessage orderMessage, SimpMessageHeaderAccessor headerAccessor) {

        System.out.println("orderMessage.getSender: " + orderMessage.getSender());

        System.out.println("jenkins5");

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

//    @PostMapping(value = "/getUserOrders")
//    public ResponseEntity<OrderResponse> getAllOrders(@RequestBody User user) {
//        List<com.binance.api.client.domain.account.Order> allOrders = new ArrayList<>();
//        OrderResponse orderResponse = new OrderResponse();
//        try {
//            for (Product product : userService.getUserProducts(user.getUserId())) {
//                List<Order> allProductOrders = client.getAllOrders(new AllOrdersRequest(product.getSymbol()));
//                allOrders.addAll(allProductOrders);
//            }
//        } catch(Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        orderResponse.setSuccess(true);
//        orderResponse.setOrders(allOrders);
//
//        return ResponseEntity.ok(orderResponse);
//    }

}
