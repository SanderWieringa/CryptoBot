package Rest.Controllers;

import com.binance.api.client.BinanceApiClientFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.binance.api.client.BinanceApiRestClient;

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

}
