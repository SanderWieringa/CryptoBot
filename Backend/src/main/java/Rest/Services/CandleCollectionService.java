package Rest.Services;

import Rest.Entities.Product;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import org.springframework.stereotype.Service;
import com.binance.api.client.domain.event.CandlestickEvent;
import java.util.List;
import java.util.TreeMap;

@Service
public class CandleCollectionService {

    private final int THIRTY_MINUTES = 1800;

    private TreeMap<Long, Candlestick> candlestickByCloseTime = new TreeMap<>();

    private ClientCreatorService clientCreatorService = new ClientCreatorService();

    private final BinanceApiRestClient client = clientCreatorService.createBinanceApiRestClient();

    private boolean orderPlaced;

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
            for (Candlestick candlestick:getCandles(product.getSymbol())) {
                addCandlestick(candlestick);
            }
        }
        System.out.println(candlestickByCloseTime);
    }

    public void addCandlestick(Candlestick candlestick) {
        candlestickByCloseTime.put(candlestick.getCloseTime(), candlestick);
    }

    public void addCandlestickEvent(CandlestickEvent candlestickEvent) {
        System.out.println("candlestickByCloseTime: " + candlestickByCloseTime.get("1637421899999"));
        Candlestick candlestick = convertCandleStickEvent(candlestickEvent);
        checkCandles(candlestick);
        candlestickByCloseTime.put(candlestick.getCloseTime(), candlestick);
    }

    private List<Candlestick> getCandles(String symbol) {
        return client.getCandlestickBars(symbol, CandlestickInterval.ONE_MINUTE);
    }

    private Candlestick getOlderCandlestick(Candlestick candlestick) {
        return candlestickByCloseTime.get(candlestick.getCloseTime() - THIRTY_MINUTES);
    }

    private boolean checkForDrop(Candlestick currentCandlestick, Candlestick oldCandlestick) {
        System.out.println("olderCandlestick: " + oldCandlestick.toString());
        System.out.println("=====================================");
        System.out.println("currentCandlestick: " + currentCandlestick.toString());
        double result = 100 - ((Float.parseFloat(currentCandlestick.getHigh()) / Float.parseFloat(oldCandlestick.getHigh())) * 100);
        if (result > 0.45) {
            return true;
        }
        return false;
    }

    private void checkCandles(Candlestick candlestick) {
        if (checkForDrop(candlestick, getOlderCandlestick(candlestick)) && !isOrderPlaced()) {
            setOrderPlaced(true);
            System.out.println("PLACE ORDER");
        }
//        if (Float.parseFloat(candlestick.getHigh()) > 61000.0 && !isOrderPlaced()) {
//            setOrderPlaced(true);
//            System.out.println("PLACE ORDER");
//        }
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
