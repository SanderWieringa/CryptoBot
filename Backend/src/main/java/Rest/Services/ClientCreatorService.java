package Rest.Services;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import org.springframework.stereotype.Service;

@Service
public class ClientCreatorService {

    private String apiKey;

    private String secret;

    private void init() {
        setApiKey();
        setSecret();
    }

    public BinanceApiWebSocketClient createBinanceApiWebSocketClient() {
        init();
        return BinanceApiClientFactory
            .newInstance(
                getApiKey(),
                getSecret()
            ).newWebSocketClient();
    }

    public BinanceApiRestClient createBinanceApiRestClient() {
        init();
        return BinanceApiClientFactory
            .newInstance(
                    getApiKey(),
                    getSecret(),
                true,
                false
            ).newRestClient();
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey() {
        this.apiKey = "bNI0EhIhLvopBAR0DKLvjdl27SAZsjkpWpi1QBcNjlXGR3nA6pqQrTbaG27HUdQg";
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret() {
        this.secret = "SxJ5xEXet7Ax9N67WandxrG4gKlPWtyEZh4ixJ0U55tVeY9sAS2nEuLLfO95iXGD";
    }
}
