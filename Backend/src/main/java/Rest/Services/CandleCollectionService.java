package Rest.Services;

import Rest.Entities.Product;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.binance.api.client.domain.event.CandlestickEvent;

import java.util.*;

@Service
public class CandleCollectionService {

    private final Long THIRTY_MINUTES = 1800000L;

    private final Long SEARCH_RANGE = 30000l;

    private TreeMap<Long, Candlestick> candlestickByCloseTime = new TreeMap<>();

    private ClientCreatorService clientCreatorService = new ClientCreatorService();

    private final BinanceApiRestClient client = clientCreatorService.createBinanceApiRestClient();

    private boolean orderPlaced;

    @Autowired
    private ProductCollectionService productCollectionService;

    private Candlestick convertCandleStickEvent(CandlestickEvent candlestickEvent) {
        Candlestick candlestick = new Candlestick();
        candlestick.setOpenTime(candlestickEvent.getOpenTime());
        candlestick.setOpen(candlestickEvent.getOpen());
        candlestick.setHigh(candlestickEvent.getHigh());
        candlestick.setLow(candlestickEvent.getLow());
        candlestick.setClose(candlestickEvent.getClose());
        candlestick.setVolume(candlestickEvent.getVolume());
        candlestick.setCloseTime(candlestickEvent.getCloseTime());
        candlestick.setQuoteAssetVolume(candlestickEvent.getQuoteAssetVolume());
        candlestick.setNumberOfTrades(candlestickEvent.getNumberOfTrades());
        candlestick.setTakerBuyBaseAssetVolume(candlestickEvent.getTakerBuyBaseAssetVolume());
        candlestick.setTakerBuyQuoteAssetVolume(candlestickEvent.getTakerBuyQuoteAssetVolume());
        return candlestick;
    }

    public void updateCandlesticks(List<Product> userCoins) {
        for (Product product:userCoins) {
            System.out.println("Symbol: " + product.getSymbol());
            try {
                for (Candlestick candlestick:getCandles(product.getSymbol())) {
                    System.out.println("CandleStick: " + candlestick.toString());
                    addCandlestick(candlestick);
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e);
                productCollectionService.removeFromWhiteList(product);
            }
        }
        System.out.println(candlestickByCloseTime);
    }

    public void addCandlestick(Candlestick candlestick) {
        candlestickByCloseTime.put(candlestick.getCloseTime(), candlestick);
    }

    public void addCandlestickEvent(CandlestickEvent candlestickEvent) {
        Candlestick candlestick = convertCandleStickEvent(candlestickEvent);
        checkCandles(candlestick);
        candlestickByCloseTime.put(candlestick.getCloseTime(), candlestick);
    }

    private List<Candlestick> getCandles(String symbol) {
        return client.getCandlestickBars(symbol, CandlestickInterval.ONE_MINUTE);
    }

    private Candlestick getOlderCandlestick(Candlestick candlestick) {
        for (long closeTime = candlestick.getCloseTime() - SEARCH_RANGE - THIRTY_MINUTES; closeTime < candlestick.getCloseTime() + SEARCH_RANGE; closeTime += 1000) {
            if (candlestickByCloseTime.get(closeTime) != null) {
                return candlestickByCloseTime.get(candlestick.getCloseTime());
            }
        }
        return null;
    }

    private boolean checkForDrop(Candlestick currentCandlestick, Candlestick oldCandlestick) {
        double result = 100 - ((Float.parseFloat(currentCandlestick.getHigh()) / Float.parseFloat(oldCandlestick.getHigh())) * 100);
        if (result > 0.40) {
            return true;
        }
        return false;
    }

    private void checkCandles(Candlestick candlestick) {
        if (checkForDrop(candlestick, getOlderCandlestick(candlestick)) && !isOrderPlaced() && getOlderCandlestick(candlestick) != null) {
            setOrderPlaced(true);
            System.out.println("PLACE ORDER");
        }
    }

    public TreeMap<Long, Candlestick> getCandlestickByCloseTime() {
        return candlestickByCloseTime;
    }

    public boolean isOrderPlaced() {
        return orderPlaced;
    }

    public void setOrderPlaced(boolean orderPlaced) {
        this.orderPlaced = orderPlaced;
    }
}
