package Rest.Entities;

import Rest.Services.ProductCollectionService;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;

@Data
@Slf4j
@Service
public class BotLogic {

    @Autowired
    private ProductCollectionService productCollectionService;

    private void subscribeCandlestickData() {

        BinanceApiWebSocketClient client = createClient();

        for (Product product:findAllProductsToSubscribe()) {
            client.onCandlestickEvent(product.getSymbol().toLowerCase(Locale.ROOT), CandlestickInterval.ONE_MINUTE, response -> {

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
                System.out.println(candleStickEvent.toString());
            });
        }
    }

    private List<Product> findAllProductsToSubscribe() {
        return productCollectionService.getProductsToTradeIn();
    }

    private BinanceApiWebSocketClient createClient() {
        return BinanceApiClientFactory
            .newInstance(
                "bNI0EhIhLvopBAR0DKLvjdl27SAZsjkpWpi1QBcNjlXGR3nA6pqQrTbaG27HUdQg",
                "SxJ5xEXet7Ax9N67WandxrG4gKlPWtyEZh4ixJ0U55tVeY9sAS2nEuLLfO95iXGD"
            ).newWebSocketClient();
    }

    public void startToListen() {
        this.subscribeCandlestickData();
    }
}
