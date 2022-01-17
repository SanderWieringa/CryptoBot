package Rest.Controllers;

import Rest.Services.CandleCollectionService;
import Rest.socketModels.MarginMessage;
import Rest.socketModels.OrderMessage;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class OrderController {

    @Autowired
    private CandleCollectionService candleCollectionService;

    @MessageMapping("/orders.send")
    @SendTo("/topic/public")
    public void sendMessage(@Payload final MarginMessage marginMessage) {
        System.out.println("marginMessage.price: " + marginMessage.getPrice());
        candleCollectionService.setPrice(marginMessage.getPrice());
    }

    @MessageMapping("/orders.newUser")
    @SendTo("/topic/public")
    public OrderMessage newUser(@Payload final OrderMessage orderMessage, SimpMessageHeaderAccessor headerAccessor) {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", orderMessage.getSender());
        return orderMessage;
    }
}
