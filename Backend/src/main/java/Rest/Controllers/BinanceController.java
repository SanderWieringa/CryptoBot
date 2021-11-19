package Rest.Controllers;

import Rest.Entities.User;
import Rest.Responses.RegisterResponse;
import Rest.Services.ClientCreatorService;
import Rest.Services.MarketService;
import Rest.Entities.Product;
import Rest.Services.UserService;
import com.binance.api.client.domain.OrderSide;
import com.binance.api.client.domain.OrderType;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.*;
import com.binance.api.client.domain.account.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.binance.api.client.BinanceApiRestClient;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/binance")
@RestController
public class BinanceController {

    private ClientCreatorService clientCreatorService = new ClientCreatorService();

    @Autowired
    private MarketService marketService;

    @Autowired
    private UserService userService;

    private final BinanceApiRestClient client = clientCreatorService.createBinanceApiRestClient();

    @GetMapping(value = "/ping")
    public void testConnection() {
        client.ping();
    }

    @GetMapping(value = "/serverTime")
    public Long getServerTime() {
        long serverTime = client.getServerTime();
        System.out.println(serverTime);

        return serverTime;
    }

    @PostMapping(value = "/placeTestMarketOrder")
    public void placeTestMarketOrder() {
        client.newOrderTest(NewOrder.marketBuy("BTCUSDT", "100"));
    }

    @GetMapping(value = "/getAllOpenOrders")
    public List<Order> getAllOpenOrders() {
        List<Order> openOrders = client.getOpenOrders(new OrderRequest("BTCUSDT"));
        System.out.println(openOrders);
        return openOrders;
    }

    @PostMapping(value = "/placeMarketOrder")
    public String placeMarketOrder() {
        NewOrderResponse newOrderResponse = client
                .newOrder(NewOrder
                        .marketBuy("BTCUSDT", "100")
                        .newOrderRespType(NewOrderResponseType.FULL));
        List<Trade> fills = newOrderResponse.getFills();
        System.out.println(newOrderResponse.getClientOrderId());

        return newOrderResponse.getClientOrderId();
    }

    @PostMapping(value = "/placeStopLossOrder")
    public String placeStopLossOrder() {
        NewOrder order = new NewOrder("BTCUSDT", OrderSide.BUY, OrderType.STOP_LOSS, TimeInForce.GTC, "0.003", "0.001");
        client.newOrder(order.stopPrice("0.001"));

        return order.getNewClientOrderId();
    }

    @PostMapping(value = "/placeTakeProfitOrder")
    public String placeTakeProfitOrder() {
        NewOrder order = new NewOrder("BTCUSDT", OrderSide.BUY, OrderType.TAKE_PROFIT, TimeInForce.GTC, "0.003", "0.001");
        client.newOrder(order.stopPrice("0.001"));

        return order.getNewClientOrderId();
    }

    @PostMapping(value = "/placeUserMarketOrders")
    public void placeUserMarketOrders(@RequestBody User user) {
        List<Product> productsToTradeIn = userService.getUserProducts(user.getUserId());
        for (Product product:productsToTradeIn) {
            NewOrderResponse newOrderResponse = client
                    .newOrder(NewOrder
                            .marketBuy(product.getName(), "0.003")
                            .newOrderRespType(NewOrderResponseType.FULL));
            System.out.println(newOrderResponse.getClientOrderId());
        }
    }

    @PostMapping(value = "/subscribe")
    public void subscribeCandlestickData(@RequestBody User user) {
        marketService.startToListen(user.getUserId());
    }

    @GetMapping(value = "/unsubscribe")
    public void unsubscribeCandlestickData() {
        marketService.stopToListen();
    }
}
