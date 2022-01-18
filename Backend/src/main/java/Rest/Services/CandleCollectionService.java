package Rest.Services;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.OrderSide;
import com.binance.api.client.domain.OrderType;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.market.Candlestick;
import org.springframework.stereotype.Service;
import com.binance.api.client.domain.event.CandlestickEvent;
import java.util.*;

@Service
public class CandleCollectionService implements ISubject {
    private final TreeMap<Long, CandlestickEvent> candlestickByCloseTime = new TreeMap<>();

    private String price = "0.001";

    private String quantity = "0.003";

    private boolean orderPlaced;

    private final ClientCreatorService clientCreatorService = new ClientCreatorService();

    private final BinanceApiRestClient client = clientCreatorService.createBinanceApiRestClient();

    private List<IObserver> subs = new ArrayList<>();

//    private Candlestick convertCandleStickEvent(CandlestickEvent candlestickEvent) {
//        Candlestick candlestick = new Candlestick();
//        candlestick.setOpenTime(candlestickEvent.getOpenTime());
//        candlestick.setOpen(candlestickEvent.getOpen());
//        candlestick.setHigh(candlestickEvent.getHigh());
//        candlestick.setLow(candlestickEvent.getLow());
//        candlestick.setClose(candlestickEvent.getClose());
//        candlestick.setVolume(candlestickEvent.getVolume());
//        candlestick.setCloseTime(candlestickEvent.getCloseTime());
//        candlestick.setQuoteAssetVolume(candlestickEvent.getQuoteAssetVolume());
//        candlestick.setNumberOfTrades(candlestickEvent.getNumberOfTrades());
//        candlestick.setTakerBuyBaseAssetVolume(candlestickEvent.getTakerBuyBaseAssetVolume());
//        candlestick.setTakerBuyQuoteAssetVolume(candlestickEvent.getTakerBuyQuoteAssetVolume());
//        return candlestick;
//    }

    public void addCandlestickEvent(CandlestickEvent candlestickEvent) {
//        Candlestick candlestick = convertCandleStickEvent(candlestickEvent);
//        candlestickEvent.getSy
        checkCandles(candlestickEvent);
        candlestickByCloseTime.put(candlestickEvent.getCloseTime(), candlestickEvent);
    }

    private CandlestickEvent getOlderCandlestick(CandlestickEvent candlestickEvent) {
        long THIRTY_MINUTES = 1800000L;
        long SEARCH_RANGE = 30000L;
        long closeTime = candlestickEvent.getCloseTime() - SEARCH_RANGE - THIRTY_MINUTES;

        for (long olderCloseTime = closeTime; olderCloseTime < candlestickEvent.getCloseTime() + SEARCH_RANGE; closeTime += 1000) {
            if (candlestickByCloseTime.get(closeTime) != null) {
                return candlestickByCloseTime.get(candlestickEvent.getCloseTime());
            }
        }
        return null;
    }

    private boolean checkForDrop(CandlestickEvent currentCandlestickEvent, CandlestickEvent oldCandlestickEvent) {
        double result = 100 - ((Float.parseFloat(currentCandlestickEvent.getHigh()) / Float.parseFloat(oldCandlestickEvent.getHigh())) * 100);
        return result > 0.40;
    }

    private void checkCandles(CandlestickEvent candlestickEvent) {
        if (checkForDrop(candlestickEvent, Objects.requireNonNull(getOlderCandlestick(candlestickEvent))) && !isOrderPlaced() && getOlderCandlestick(candlestickEvent) != null) {
            placeOrder(candlestickEvent);
            setOrderPlaced(true);
            System.out.println("PLACE ORDER");
        }
    }

    public void placeOrder(CandlestickEvent candlestickEvent){
        NewOrder order = NewOrder.marketBuy(candlestickEvent.getSymbol(), getQuantity());
        client.newOrder(order);
        notifySubs();
    }

    public boolean isOrderPlaced() {
        return orderPlaced;
    }

    public void setOrderPlaced(boolean orderPlaced) {
        this.orderPlaced = orderPlaced;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public void subscribe(IObserver sub) {
        subs.add(sub);
    }

    @Override
    public void unsubscribe(IObserver sub) {
        subs.remove(sub);
    }

    @Override
    public void notifySubs() {
        for (IObserver sub : subs) {
            sub.update();
        }
    }
}
