package Rest.Controllers;

import com.binance.api.client.BinanceApiClientFactory;
import org.springframework.web.bind.annotation.RestController;
import com.binance.api.client.BinanceApiRestClient;

@RestController
public class BinanceController {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("JUkhO1fMp1jNRuuU53Lop6Cgx1Aowdt5amXtYYtSfAHruJt631G1j5RMdeRDs4iS", "R3ptOSMImlUTtohVpIu0hghVnoCjHjmXG8PIV537DKTOlMgIcP1vgDGB7kcPE5x5");
    BinanceApiRestClient client = factory.newRestClient();
}
