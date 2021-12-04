package Rest.Controllers;

import Rest.Entities.User;
import Rest.Responses.LoginResponse;
import Rest.Responses.OrderResponse;
import Rest.Responses.RegisterResponse;
import Rest.Services.ClientCreatorService;
import Rest.Services.MarketService;
import Rest.Entities.Product;
import Rest.Services.UserService;
import com.binance.api.client.domain.OrderSide;
import com.binance.api.client.domain.OrderType;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.*;
import com.binance.api.client.domain.account.request.AllOrdersRequest;
import com.binance.api.client.domain.account.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.binance.api.client.BinanceApiRestClient;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/binance")
@RestController
public class BinanceController {

    private final ClientCreatorService clientCreatorService = new ClientCreatorService();

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


    @PostMapping(value = "/getUserOrders")
    public ResponseEntity<OrderResponse> getAllOrders(@RequestBody User user) {
        List<List<Order>> allOrders = new ArrayList<>();
        OrderResponse orderResponse = new OrderResponse();
        try {
            for (Product product : userService.getUserProducts(user.getUserId())) {
                List<Order> allProductOrders = client.getAllOrders(new AllOrdersRequest(product.getSymbol()));
                allOrders.add(allProductOrders);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        orderResponse.setSuccess(true);
        orderResponse.setOrders(allOrders);

        return ResponseEntity.ok(orderResponse);
    }

    @PostMapping(value = "/getAllOpenOrders")
    public ResponseEntity<OrderResponse> getAllOpenOrders(@RequestBody User user) {
        List<List<Order>> allOpenOrders = new ArrayList<>();
        OrderResponse orderResponse = new OrderResponse();
        try {
            for (Product product : userService.getUserProducts(user.getUserId())) {
                List<Order> AllOpenOrders = client.getOpenOrders(new OrderRequest(product.getSymbol()));
                allOpenOrders.add(AllOpenOrders);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        orderResponse.setSuccess(true);
        orderResponse.setOrders(allOpenOrders);

        return ResponseEntity.ok(orderResponse);
    }

    @PostMapping(value = "/placeMarketOrder")
    public ResponseEntity<String> placeMarketOrder() {
        NewOrderResponse newOrderResponse = client
                .newOrder(NewOrder
                        .marketBuy("BTCUSDT", "100")
                        .newOrderRespType(NewOrderResponseType.FULL));

        try {

            List<Trade> fills = newOrderResponse.getFills();
            System.out.println(newOrderResponse.getClientOrderId());
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(newOrderResponse.getClientOrderId());
    }

    @PostMapping(value = "/placeStopLossOrder")
    public ResponseEntity<String> placeStopLossOrder() {
        NewOrder order = new NewOrder("BTCUSDT", OrderSide.BUY, OrderType.STOP_LOSS, TimeInForce.GTC, "0.003", "0.001");
        try {
            client.newOrder(order.stopPrice("0.001"));
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(order.getNewClientOrderId());
    }

    @PostMapping(value = "/placeTakeProfitOrder")
    public ResponseEntity<String> placeTakeProfitOrder() {
        NewOrder order = new NewOrder("BTCUSDT", OrderSide.BUY, OrderType.TAKE_PROFIT, TimeInForce.GTC, "0.003", "0.001");
        try {
            client.newOrder(order.stopPrice("0.001"));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(order.getNewClientOrderId());
    }

    @PostMapping(value = "/placeUserMarketOrders")
    public ResponseEntity<Boolean> placeUserMarketOrders(@RequestBody User user) {
        List<Product> productsToTradeIn = userService.getUserProducts(user.getUserId());

        try {
            for (Product product : productsToTradeIn) {
                NewOrderResponse newOrderResponse = client
                        .newOrder(NewOrder
                                .marketBuy(product.getName(), "0.003")
                                .newOrderRespType(NewOrderResponseType.FULL));
                System.out.println(newOrderResponse.getClientOrderId());
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(true);
    }

    @PostMapping(value = "/subscribe")
    public ResponseEntity<Boolean> subscribeCandlestickData(@RequestBody User user) {
        try {
            marketService.startToListen(user.getUserId());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(true);
    }

    @GetMapping(value = "/unsubscribe")
    public ResponseEntity<Boolean> unsubscribeCandlestickData() {
        try {
            marketService.stopToListen();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(true);
    }
}
