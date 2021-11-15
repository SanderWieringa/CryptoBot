package Rest.Services;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import org.springframework.stereotype.Service;

@Service
public class ClientCreatorService {

    private String apiKey = "bNI0EhIhLvopBAR0DKLvjdl27SAZsjkpWpi1QBcNjlXGR3nA6pqQrTbaG27HUdQg";

    private String secret = "SxJ5xEXet7Ax9N67WandxrG4gKlPWtyEZh4ixJ0U55tVeY9sAS2nEuLLfO95iXGD";

    public BinanceApiWebSocketClient createBinanceApiWebSocketClient() {
        return BinanceApiClientFactory
            .newInstance(
                    apiKey,
                    secret
            ).newWebSocketClient();
    }

    public BinanceApiRestClient createBinanceApiRestClient() {
        return BinanceApiClientFactory
            .newInstance(
                    apiKey,
                    secret,
                true,
                false
            ).newRestClient();
    }
}
