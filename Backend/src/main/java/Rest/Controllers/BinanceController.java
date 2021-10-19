package Rest.Controllers;

import Rest.Entities.Product;
import Rest.Services.ProductCollectionService;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.binance.api.client.domain.account.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.binance.api.client.BinanceApiRestClient;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/binance")
@RestController
public class BinanceController {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(
            "bNI0EhIhLvopBAR0DKLvjdl27SAZsjkpWpi1QBcNjlXGR3nA6pqQrTbaG27HUdQg",
            "SxJ5xEXet7Ax9N67WandxrG4gKlPWtyEZh4ixJ0U55tVeY9sAS2nEuLLfO95iXGD",
            true,
            false
    );
    BinanceApiRestClient client = factory.newRestClient();

    @Autowired
    private ProductCollectionService productCollectionService;

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

    @PostMapping(value = "/placeMarketOrder")
    public String placeMarketOrder() {
        NewOrderResponse newOrderResponse = client
                .newOrder(NewOrder
                        .marketBuy("BTCUSDT", "1")
                        .newOrderRespType(NewOrderResponseType.FULL));
        List<Trade> fills = newOrderResponse.getFills();
        System.out.println(newOrderResponse.getClientOrderId());

        return newOrderResponse.getClientOrderId();
    }

    @PostMapping(value = "/placeUserMarketOrders")
    public void placeUserMarketOrders() {
        List<Product> productsToTradeIn = productCollectionService.getProductsToTradeIn();
        for (Product product:productsToTradeIn) {
            NewOrderResponse newOrderResponse = client
                    .newOrder(NewOrder
                            .marketBuy(product.getName(), "0.003")
                            .newOrderRespType(NewOrderResponseType.FULL));
        }
    }


}
