package Rest.socketController;

import Rest.socketModels.OrderMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class OrderController {
    @MessageMapping("/orders.send")
    @SendTo("/topic/public")
    public OrderMessage sendMessage(@Payload final OrderMessage orderMessage) {
        return orderMessage;
    }

    @MessageMapping("/orders.newUser")
    @SendTo("/topic/public")
    public OrderMessage newUser(@Payload final OrderMessage orderMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", orderMessage.getSender());
        return orderMessage;
    }
}
