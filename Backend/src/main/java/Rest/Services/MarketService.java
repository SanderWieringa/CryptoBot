package Rest.Services;

import Rest.Entities.Product;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

@Service
public class MarketService {

    private final ClientCreatorService clientCreatorService = new ClientCreatorService();

    @Autowired
    private UserService userService;

    @Autowired
    private CandleCollectionService candleCollectionService;

    private final BinanceApiWebSocketClient client = clientCreatorService.createBinanceApiWebSocketClient();

    private boolean isRunning;

    private Closeable socket;

    private void subscribeCandlestickData(int userId) {
        isRunning = true;

        for (Product product:findAllProductsToSubscribe(userId)) {
            final String channel = product.getSymbol().toLowerCase();
            socket = client.onCandlestickEvent(channel, CandlestickInterval.ONE_MINUTE, response -> {

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

                    Boolean barFinal = candleStickEvent.getBarFinal();
                    if (Boolean.TRUE.equals(barFinal)) {
                        candleCollectionService.addCandlestickEvent(candleStickEvent);
                    }
                    System.out.println(candleStickEvent.toString());
                } else {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private List<Product> findAllProductsToSubscribe(int userId) {
        return userService.getUserProducts(userId);
    }

    public void startToListen(int userId) {
        this.subscribeCandlestickData(userId);
    }

    public void stopToListen() {
        isRunning = false;
    }
}
