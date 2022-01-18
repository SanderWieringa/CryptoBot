package Rest.Controllers;

import Rest.Entities.User;
import Rest.Responses.*;
import Rest.Services.*;
import Rest.Entities.Product;
import com.binance.api.client.domain.OrderSide;
import com.binance.api.client.domain.OrderType;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.*;
import com.binance.api.client.domain.account.request.AllOrdersRequest;
import com.binance.api.client.domain.account.request.OrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
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
public class BinanceController implements ISubject {

    OkHttpClient httpClient = new OkHttpClient();

    private final ClientCreatorService clientCreatorService = new ClientCreatorService();

    private final BinanceApiRestClient client = clientCreatorService.createBinanceApiRestClient();

    @Autowired
    private MarketService marketService;

    @Autowired
    private UserService userService;

    @Autowired
    private CandleCollectionService candleCollectionService;

    private List<IObserver> subs = new ArrayList<>();

    @GetMapping(value = "/list")
    public ResponseEntity<GetProductCollectionResponse> getAllProducts() {
        GetProductCollectionResponse productResponse = new GetProductCollectionResponse();
        List<Product> products = new ArrayList<>();
        Request request = new Request.Builder()
                .url("https://www.binance.com/bapi/composite/v1/public/marketing/symbol/list")
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            assert response.body() != null;
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray productList = jsonObject.getJSONArray("data");
            ObjectMapper objectMapper = new ObjectMapper();

            for (int i = 0; i < productList.length(); i++) {
                Product product = objectMapper.readValue(productList.get(i).toString(), Product.class);
                products.add(product);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        productResponse.setSuccess(true);
        productResponse.setProducts(products);

        return ResponseEntity.ok(productResponse);
    }

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
        List<Order> allOrders = new ArrayList<>();
        OrderResponse orderResponse = new OrderResponse();
        try {
            for (Product product : userService.getUserProducts(user.getUserId())) {
                List<Order> allProductOrders = client.getAllOrders(new AllOrdersRequest(product.getSymbol()));
                allOrders.addAll(allProductOrders);
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
        List<Order> allOpenOrders = new ArrayList<>();
        OrderResponse orderResponse = new OrderResponse();
        try {
            for (Product product : userService.getUserProducts(user.getUserId())) {
                List<Order> AllOpenOrders = client.getOpenOrders(new OrderRequest(product.getSymbol()));
                allOpenOrders.addAll(AllOpenOrders);
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
                        .marketBuy("BTCUSDT", "0.003")
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

    @PostMapping(value = "/placeUserTakeProfitOrder")
    public ResponseEntity<PlaceOrderResponse> placeTakeProfitOrder(@RequestBody User user) {
        List<Product> productsToTradeIn = userService.getUserProducts(user.getUserId());
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        try {
            for (Product product : productsToTradeIn) {
                NewOrder order = NewOrder.marketBuy(product.getSymbol(), candleCollectionService.getQuantity());
//                order.stopPrice(candleCollectionService.getPrice());
//                NewOrder order = NewOrder.marketBuy(product.getSymbol(), "0.003");
//                NewOrder order1 = order.stopPrice(candleCollectionService.getPrice());
//                client.newOrderTest(order1);
//                NewOrder order = new NewOrder(product.getSymbol(), OrderSide.BUY, OrderType.TAKE_PROFIT, TimeInForce.GTC, "0.003");
//                client.newOrder(order);
//                NewOrder order1 = NewOrder.marketBuy(product.getSymbol(), "0.003");
                //                order.stopPrice(candleCollectionService.getPrice());
//                NewOrder order2 = new NewOrder(product.getSymbol(), OrderSide.BUY, OrderType.TAKE_PROFIT, TimeInForce.GTC, "0.003");
//                client.newOrder(order2);
//                NewOrder order = new NewOrder( product.getSymbol(), OrderSide.BUY, OrderType.TAKE_PROFIT, "0.003");

//                NewOrder order = (NewOrder.marketBuy(product.getSymbol(), "0.003")).stopPrice(candleCollectionService.getPrice());
//                NewOrder order = new NewOrder(product.getSymbol(), OrderSide.BUY, OrderType.TAKE_PROFIT, TimeInForce.IOC, "0.003", "0");
                client.newOrder(order);
                notifySubs();

            }

        placeOrderResponse.setSuccess(true);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(placeOrderResponse);
    }

    @PostMapping(value = "/placeUserMarketOrders")
    public ResponseEntity<Boolean> placeUserMarketOrders(@RequestBody User user) {
        List<Product> productsToTradeIn = userService.getUserProducts(user.getUserId());

        try {
            for (Product product : productsToTradeIn) {
                NewOrderResponse newOrderResponse = client
                        .newOrder(NewOrder
                                .marketBuy(product.getSymbol(), "0.003")
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

    @Override
    public void subscribe(IObserver sub) {
        subs.add(sub);
    }

    @Override
    public void unsubscribe(IObserver sub) {
        subs.remove(sub);
    }

    @Override
    public void notifySubs() {
        for (IObserver sub : subs) {
            sub.update();
        }
    }
}
