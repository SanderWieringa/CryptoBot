package Rest.Controllers;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.binance.api.client.domain.account.Trade;
import org.springframework.web.bind.annotation.*;
import com.binance.api.client.BinanceApiRestClient;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/binance")
@RestController
public class BinanceController {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("JUkhO1fMp1jNRuuU53Lop6Cgx1Aowdt5amXtYYtSfAHruJt631G1j5RMdeRDs4iS", "R3ptOSMImlUTtohVpIu0hghVnoCjHjmXG8PIV537DKTOlMgIcP1vgDGB7kcPE5x5");
    BinanceApiRestClient client = factory.newRestClient();

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
                        .marketBuy("LINKETH", "1000")
                        .newOrderRespType(NewOrderResponseType.FULL));
        List<Trade> fills = newOrderResponse.getFills();
        System.out.println(newOrderResponse.getClientOrderId());

        return newOrderResponse.getClientOrderId();
    }
}
