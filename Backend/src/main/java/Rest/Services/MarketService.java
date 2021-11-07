package Rest.Services;

import Rest.Entities.Product;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;

@Service
public class MarketService {

    @Autowired
    private UserService userService;

    @Autowired
    private CandleCollectionService candleCollectionService;

    private void subscribeCandlestickData(int userId) {
    boolean isRunning = false;

        BinanceApiWebSocketClient client = createClient();
        isRunning = true;

        for (Product product:findAllProductsToSubscribe(userId)) {
            client.onCandlestickEvent(product.getSymbol().toLowerCase(Locale.ROOT), CandlestickInterval.ONE_MINUTE, response -> {

                if (isRunning){
                    CandlestickEvent candleStickEvent = new CandlestickEvent();
                    candleStickEvent.setEventType(response.getEventType());
                    candleStickEvent.setEventTime(response.getEventTime());
                    candleStickEvent.setSymbol(response.getSymbol());
                    candleStickEvent.setOpenTime(response.getOpenTime());
                    candleStickEvent.setOpen(response.getOpen());
                    candleStickEvent.setClose(response.getClose());
                    candleStickEvent.setHigh(response.getHigh());
                    candleStickEvent.setLow(response.getLow());
                    candleStickEvent.setVolume(response.getVolume());
                    candleStickEvent.setCloseTime(response.getCloseTime());
                    candleStickEvent.setIntervalId(response.getIntervalId());
                    candleStickEvent.setFirstTradeId(response.getFirstTradeId());
                    candleStickEvent.setLastTradeId(response.getLastTradeId());
                    candleStickEvent.setQuoteAssetVolume(response.getQuoteAssetVolume());
                    candleStickEvent.setNumberOfTrades(response.getNumberOfTrades());
                    candleStickEvent.setTakerBuyBaseAssetVolume(response.getTakerBuyBaseAssetVolume());
                    candleStickEvent.setTakerBuyQuoteAssetVolume(response.getTakerBuyQuoteAssetVolume());
                    candleStickEvent.setBarFinal(response.getBarFinal());
                    candleCollectionService.addCandle(candleStickEvent);
                    System.out.println(candleStickEvent.toString());
                }
            });
        }
    }

    private List<Product> findAllProductsToSubscribe(int userId) {
        return userService.getUserProducts(userId);
    }

    private BinanceApiWebSocketClient createClient() {
        return BinanceApiClientFactory
            .newInstance(
                "bNI0EhIhLvopBAR0DKLvjdl27SAZsjkpWpi1QBcNjlXGR3nA6pqQrTbaG27HUdQg",
                "SxJ5xEXet7Ax9N67WandxrG4gKlPWtyEZh4ixJ0U55tVeY9sAS2nEuLLfO95iXGD"
            ).newWebSocketClient();
    }

    public void startToListen(int userId) {
        this.subscribeCandlestickData(userId);
    }

    public void stopToListen() {
        isRunning = false;
    }
}
